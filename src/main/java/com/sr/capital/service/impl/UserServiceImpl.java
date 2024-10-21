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
        User user =userRepository.findBySrUserId(Long.valueOf(userDetails.getUserId()));
       if(user==null){
           user =User.mapUser(userDetails);
       } else if (user.getPanNumber() == null) {
           User.mapUpdateUser(userDetails, user);
       } else {
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
                    response.setMobile(aes256.decrypt(user.getMobile()));
                    response.setPanNumber(aes256.decrypt(user.getPanNumber()));
                    response.setDateOfBirth(aes256.decrypt(user.getDateOfBirth()));
                    response.setFatherName(aes256.decrypt(user.getFatherName()));
                    response.setGender(user.getGender());
                    response.setIsMobileVerified(user.getIsMobileVerified());
                    response.setCurrentAccountAvailable(user.getCurrentAccountAvailable());
                }catch (Exception ex){
                 /*   EncryptionConfig encryptionConfig =new EncryptionConfig();
                    encryptionConfig.setKey("test");
                    AES256 aes2561 = new AES256(encryptionConfig);

                    response.setFirstName(aes2561.decrypt(user.getFirstName()));
                    response.setMobile(aes2561.decrypt(user.getMobile()));
                    response.setPanNumber(aes2561.decrypt(user.getPanNumber()));
                    response.setDateOfBirth(aes2561.decrypt(user.getDateOfBirth()));
                    response.setFatherName(aes2561.decrypt(user.getFatherName()));*/

                }
            }else{
                response.setIsMobileVerified(true);
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
              /*  EncryptionConfig encryptionConfig =new EncryptionConfig();
                encryptionConfig.setKey("test");
                AES256 aes2561 = new AES256(encryptionConfig);
                user.setFirstName(aes2561.decrypt(user.getFirstName()));
                user.setMobile(aes2561.decrypt(user.getMobile()));
                user.setEmail(aes2561.decrypt(user.getEmail()));
                user.setLastName(aes2561.decrypt(user.getLastName()));
                user.setMiddleName(aes2561.decrypt(user.getMiddleName()));
                user.setDateOfBirth(aes2561.decrypt(user.getDateOfBirth()));
                user.setPanNumber(aes2561.decrypt(user.getPanNumber()));

                User user1  =userRepository.findTopBySrCompanyId(srCompanyId);
                user1.setEmail(aes256.encrypt(user.getEmail()));
                user1.setMobile(aes256.encrypt(user.getMobile()));
                user1.setFirstName(aes256.encrypt(user.getFirstName()));
                user1.setLastName(aes256.encrypt(user.getLastName()));
                user1.setMiddleName(aes256.encrypt(user.getMiddleName()));
                user1.setPanNumber(aes256.encrypt(user.getPanNumber()));
                user1.setDateOfBirth(aes256.encrypt(user.getDateOfBirth()));
                user1.setFatherName(aes256.encrypt(user.getFatherName()));
                userRepository.save(user1);*/


            }
        }
        return user;
    }

    @Override
    public ValidateMobileResponse validateMobileNumber(String mobileNumber) {
        return null;
    }

    @Override
    public UserProgressResponseDto getCompanyCompanyProgressState() {

        List<LoanApplication> loanApplication = loanApplicationRepository.findBySrCompanyIdAndLoanStatus(Long.valueOf(RequestData.getTenantId()), LoanStatus.PENDING);
        String currentState = Screens.LOAN_DETAILS.name();
        if(CollectionUtils.isNotEmpty(loanApplication)){
            currentState = Screens.PERSONAL_INFO.name();

            User user = userRepository.findTopBySrCompanyId(Long.valueOf(RequestData.getTenantId()));

            if(user!=null){
                currentState = userProgressService.getUserProgress(RequestData.getTenantId());

            }
        }
        return UserProgressResponseDto.builder().screenName(currentState).build();
    }


}
