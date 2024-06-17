package com.sr.capital.validation;


import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.exception.verification.ExpiredOtpException;
import com.sr.capital.exception.verification.IncorrectOtpException;
import com.sr.capital.exception.verification.InvalidVerificationTokenException;
import com.sr.capital.exception.verification.OtpFailedAttemptsLimitException;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

@Component
@AllArgsConstructor
public class VerifyOtpRequestValidator implements RequestValidator {


    final AppProperties appProperties;


    final VerificationEntityServiceImpl verificationManager;

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> T validateRequest(U request) throws Exception {
        VerifyOtpRequest otpRequest = (VerifyOtpRequest) request;
        VerificationEntity verificationEntity = null;//verificationManager.findByVerificationToken(otpRequest.getVerificationToken());
        if(ObjectUtils.isEmpty(verificationEntity) || !VerificationType.OTP.equals(verificationEntity.getType())
                || Boolean.FALSE.equals(verificationEntity.getIsEnabled())) {
            throw new InvalidVerificationTokenException();
        }
        if(verificationEntity.hasExpired(LocalDateTime.now())) {
            throw new ExpiredOtpException();
        }
        if(verificationEntity.getFailedCounter() >= appProperties.getOtpMaxFailureCount()){
            throw new OtpFailedAttemptsLimitException();
        }

        if(!otpRequest.getOtp().equals(verificationEntity.getData())){
            verificationEntity.setFailedCounter(verificationEntity.getFailedCounter()+1);
            verificationManager.saveVerificationEntity(verificationEntity);
            throw new IncorrectOtpException();
        }

        return (T) verificationEntity;
    }
}
