package com.sr.capital.service.impl;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.TenantDetails;
import com.sr.capital.dto.request.UserDetails;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.response.UserProgressResponseDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.User;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.ValidateMobileResponse;
import com.sr.capital.helpers.enums.*;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.repository.primary.UserRepository;
import com.sr.capital.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    final ShiprocketClient shiprocketClient;

    final UserRepository userRepository;

    final AES256 aes256;

    final VerificationUtilService verificationUtilService;

    final LoanApplicationRepository loanApplicationRepository;

    final UserProgressServiceImpl userProgressService;

    @Override
    public ApiTokenUserDetailsResponse getUserDetails(String token) {
        return shiprocketClient.getUserDetailsUsingApiToken(token);
    }

    @Override
    public UserDetails saveUserDetails(UserDetails userDetails) throws CustomException {

        validateUserDetails(userDetails);
        encryptData(userDetails);
        User user =userRepository.findTopBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
       if(user==null){
           user =User.mapUser(userDetails);
       } else {
           User.mapUpdateUser(userDetails, user);
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

        verificationUtilService.createVerificationInstanceAndCommunicate(verificationOrchestratorRequest, String.valueOf(userDetails.getSrCompanyId()));
        return verificationOrchestratorRequest;
    }

    private void encryptData(UserDetails userDetails) {
        userDetails.setEmail(aes256.encrypt(userDetails.getEmail()));
        userDetails.setMobileNumber(aes256.encrypt(userDetails.getMobileNumber()));
        userDetails.setFirstName(aes256.encrypt(userDetails.getFirstName()));
        userDetails.setLastName(aes256.encrypt(userDetails.getLastName()));
        userDetails.setMiddleName(aes256.encrypt(userDetails.getMiddleName()));
        userDetails.setPanNumber(aes256.encrypt(userDetails.getPanNumber()));
        userDetails.setDateOfBirth(aes256.encrypt(userDetails.getDateOfBirth()));
        userDetails.setFatherName(aes256.encrypt(userDetails.getFatherName()));
    }

    @Override
    public InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token) {
        InternalTokenUserDetailsResponse response = shiprocketClient.getUserDetailsUsingInternalToken(token);

        if(response.getCompanyId()!=null){
            User user = userRepository.findTopBySrCompanyId(Long.valueOf(response.getCompanyId()));
            if(user!=null){
                try {
                    response.setCompanyName(user.getCompanyName());
                    response.setIsAccepted(user.getIsAccepted());
                    response.setEntityType(user.getEntityType());
                    response.setFirstName(aes256.decrypt(user.getFirstName()));
                    response.setEmail(aes256.decrypt(user.getEmail()));
                    response.setMobile(aes256.decrypt(user.getMobile()));
                    response.setPanNumber(aes256.decrypt(user.getPanNumber()));
                    response.setDateOfBirth(aes256.decrypt(user.getDateOfBirth()));
                    response.setFatherName(aes256.decrypt(user.getFatherName()));
                    response.setGender(user.getGender());
                    response.setIsMobileVerified(user.getIsMobileVerified());
                    response.setCurrentAccountAvailable(user.getCurrentAccountAvailable());
                }catch (Exception ex){

                }
            }else{
                response.setIsMobileVerified(true);
                addUserIfEmpty(response);
            }
        }
        return response;
    }

    private void saveSellerData(InternalTokenUserDetailsResponse response) {
        User user =User.builder().firstName(aes256.encrypt(response.getFirstName())).srUserId(Long.valueOf(response.getUserId()))
                .srCompanyId(Long.valueOf(response.getCompanyId())).companyName(response.getCompanyName()).build();
        userRepository.save(user);
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
            try {
                user.setFirstName(aes256.decrypt(user.getFirstName()));
                user.setMobile(aes256.decrypt(user.getMobile()));
                user.setEmail(aes256.decrypt(user.getEmail()));
                user.setLastName(aes256.decrypt(user.getLastName()));
                user.setMiddleName(aes256.decrypt(user.getMiddleName()));
                user.setDateOfBirth(aes256.decrypt(user.getDateOfBirth()));
                user.setPanNumber(aes256.decrypt(user.getPanNumber()));
                user.setFatherName(aes256.decrypt(user.getFatherName()));
            }catch (Exception ex) {//temp code

            }
        }
        return user;
    }

    @Override
    public UserDetails getCompanyDetailsWithoutEncryption(Long srCompanyId) {
        User user= userRepository.findTopBySrCompanyId(srCompanyId);
        UserDetails userDetails = null;
        if(user!=null) {
            userDetails =new UserDetails();
            try {
                userDetails.setFirstName(aes256.decrypt(user.getFirstName()));
                userDetails.setMobileNumber(aes256.decrypt(user.getMobile()));
                userDetails.setEmail(aes256.decrypt(user.getEmail()));
                userDetails.setLastName(aes256.decrypt(user.getLastName()));
                userDetails.setMiddleName(aes256.decrypt(user.getMiddleName()));
                userDetails.setDateOfBirth(aes256.decrypt(user.getDateOfBirth()));
                userDetails.setPanNumber(aes256.decrypt(user.getPanNumber()));
                userDetails.setFatherName(aes256.decrypt(user.getFatherName()));
                userDetails.setCurrentAccountAvailable(user.getCurrentAccountAvailable());
                userDetails.setComments(user.getComments());
                userDetails.setIsAccepted(user.getIsAccepted());
                userDetails.setEntityType(user.getEntityType());
                userDetails.setCompanyName(user.getCompanyName());
                userDetails.setGender(user.getGender());
                userDetails.setIsMobileNumberVerified(user.getIsMobileVerified());
                userDetails.setCurrentAccountAvailable(user.getCurrentAccountAvailable());
            }catch (Exception ex) {//temp code

            }
        }
        return userDetails;
    }

    @Override
    public ValidateMobileResponse validateMobileNumber(String mobileNumber) {
        return null;
    }

    @Override
    public UserProgressResponseDto getCompanyCompanyProgressState() {

        return userProgressService.getUserProgress(RequestData.getTenantId());
    }



    private void addUserIfEmpty(InternalTokenUserDetailsResponse userDetails) {
            UserDetails userDetailsToSave = new UserDetails();
            userDetailsToSave.setUserId(userDetails.getUserId());
            userDetailsToSave.setFirstName(userDetails.getFirstName());
            userDetailsToSave.setLastName(userDetails.getLastName());
            userDetailsToSave.setCompanyId(Long.valueOf(userDetails.getCompanyId()));
            userDetailsToSave.setCompanyName(userDetails.getCompanyName());
            userDetailsToSave.setEmail(userDetails.getEmail());
            userDetailsToSave.setMobileNumber(userDetails.getMobile());
            userDetailsToSave.setIsMobileNumberVerified(true);
        try {
            saveUserDetails(userDetailsToSave);
        } catch (CustomException e) {

        }

    }
}
