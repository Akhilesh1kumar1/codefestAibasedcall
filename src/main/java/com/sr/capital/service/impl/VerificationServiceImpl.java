package com.sr.capital.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.ResendOtpRequest;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.primary.User;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.VerificationService;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.OtpGenerator;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    final RequestValidationStrategy requestValidationStrategy;
    final VerificationEntityServiceImpl verificationEntityService;
    final UserServiceImpl userService;
    final AppProperties appProperties;
    final CommunicationService communicationService;

    @Override
    public Boolean verifyPan(String value, RequestType requestType) throws Exception {
        Boolean result =requestValidationStrategy.validateRequest(value,requestType);

        return result;
    }

    @Override
    public Boolean verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception {
        VerificationEntity verificationEntity = requestValidationStrategy.validateRequest(verifyOtpRequest,RequestType.MOBILE);
        verificationEntityService.softDeleteVerificationEntity(verificationEntity);
        userService.updateVerifyFlag(verificationEntity.getUserId());
        return true;
    }

    @Override
    public Boolean verifyGst(String value) throws Exception {
        return requestValidationStrategy.validateRequest(value,RequestType.GST);
    }

    public Boolean resendOtp(ResendOtpRequest resendOtpRequest) throws Exception {
        VerificationOrchestratorRequest verificationOrchestratorRequest = VerificationOrchestratorRequest.builder().build();
        String mobile = null;
        VerificationEntity verificationEntity = requestValidationStrategy.validateRequest(resendOtpRequest,RequestType.RESEND_OTP);
        if(verificationEntity==null){
            throw new CustomException("Invalid Request", HttpStatus.BAD_REQUEST);
        }

        verificationEntity.setData(OtpGenerator.generateOTP());
        if(!"prod".equals(appProperties.getActiveProfile())) {
            verificationEntity.setData("654321");
        }
        verificationEntity.setFailedCounter(0);
        verificationEntity.setRequestCounter(verificationEntity.getRequestCounter()+1);
        verificationEntity.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        verificationEntityService.saveVerificationEntity(verificationEntity);

        //TODO: Communication
        communicationService.sendOtpForVerification(communicationService.getCommunicationRequestForOtpVerificationViaSmsAndWhatsApp(mobile, verificationEntity.getData(), List.of(verificationEntity.getData())));
        return true;
    }


}
