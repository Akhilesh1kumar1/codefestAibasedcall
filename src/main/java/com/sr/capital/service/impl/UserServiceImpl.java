package com.sr.capital.service.impl;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.TenantDetails;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.repository.primary.UserRepository;
import com.sr.capital.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

   final ShiprocketClient shiprocketClient;

   final UserRepository userRepository;

    final AES256 aes256;
    final VerificationUtilService verificationUtilService;

    @Override
    public ApiTokenUserDetailsResponse getUserDetails(String token) {
        return shiprocketClient.getUserDetailsUsingApiToken(token);
    }

    @Override
    public UserDetails saveUserDetails(UserDetails userDetails) throws CustomException {

        validateUserDetails(userDetails);
        encryptData(userDetails);
        User user =userRepository.findBySrUserId(Long.valueOf(userDetails.getUserId()));
       if(user==null){
           user =User.mapUser(userDetails);
       }else {
           throw new CustomException("User already exist",HttpStatus.BAD_REQUEST);
       }

        userRepository.save(user);
        userDetails.setId(user.getId());
        if(!userDetails.getIsMobileNumberVerified()){
            VerificationOrchestratorRequest verificationOrchestratorRequest = sendOtp(user);
             userDetails.setVerificationToken(verificationOrchestratorRequest.getVerificationEntity().getId());
        }


        return userDetails;
    }

    private VerificationOrchestratorRequest sendOtp(User userDetails) throws CustomException {
        TenantDetails tenantDetails =TenantDetails.builder().capitalUserId(userDetails.getId()).mobileNumber(aes256.decrypt(userDetails.getMobile())).srUserId(userDetails.getSrUserId()).emailId(aes256.decrypt(userDetails.getEmail())).build();

        VerificationOrchestratorRequest verificationOrchestratorRequest = VerificationOrchestratorRequest.builder()
                .rawRequest(
                        VerificationOrchestratorRequest.RawRequest.builder()
                                .callbackType(CallbackType.USER_SIGN_UP)
                                .verificationType(VerificationType.OTP)
                                .entityId(userDetails.getId())
                                .channel(CommunicationChannels.SMS_WHATSAPP)
                                .build()
                ).tenantDetails(tenantDetails)
                .build();

        verificationUtilService.createVerificationInstanceAndCommunicate(verificationOrchestratorRequest);
        return verificationOrchestratorRequest;
    }

    private void encryptData(UserDetails userDetails) {
        userDetails.setEmail(aes256.encrypt(userDetails.getEmail()));
        userDetails.setMobileNumber(aes256.encrypt(userDetails.getMobileNumber()));
        userDetails.setFirstName(aes256.encrypt(userDetails.getFirstName()));
        userDetails.setLastName(aes256.encrypt(userDetails.getLastName()));
        userDetails.setMiddleName(aes256.encrypt(userDetails.getMiddleName()));
        userDetails.setPanNumber(aes256.encrypt(userDetails.getPanNumber()));
    }

    @Override
    public InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token) {
        InternalTokenUserDetailsResponse response = shiprocketClient.getUserDetailsUsingInternalToken(token);

        if(response.getCompanyId()!=null){
            User user = userRepository.findTopBySrCompanyId(Long.valueOf(response.getCompanyId()));
            if(user!=null){
                response.setFirstName(aes256.decrypt(response.getFirstName()));
                response.setCompanyName(response.getCompanyName());
                response.setMobile(aes256.decrypt(user.getMobile()));
                response.setIsAccepted(user.getIsAccepted());
                response.setEntityType(user.getEntityType());
                response.setPanNumber(aes256.decrypt(user.getPanNumber()));
            }
        }
        return response;
    }

    private void validateUserDetails(UserDetails userDetails) throws CustomException {

        /*if(RequestData.getUserId().longValue()!=Long.valueOf(userDetails.getUserId())){
            throw new CustomException("Invalid UserId ", HttpStatus.BAD_REQUEST);
        }*/

        if(!RequestData.getTenantId().equalsIgnoreCase(String.valueOf(userDetails.getCompanyId()))){
            throw new CustomException("Invalid CompanyId ", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public boolean updateVerifyFlag(Long userId){
        User user = userRepository.findById(userId).orElse(null);
        if(user!=null){
            user.setIsMobileVerified(true);
            userRepository.save(user);
        }
        return true;
    }

    @Override
    public User getCompanyDetails(Long srCompanyId) {
        User user= userRepository.findTopBySrCompanyId(srCompanyId);
        if(user!=null) {
            user.setFirstName(aes256.decrypt(user.getFirstName()));
            user.setMobile(aes256.decrypt(user.getMobile()));
            user.setEmail(aes256.decrypt(user.getEmail()));
        }
        return user;
    }

}
