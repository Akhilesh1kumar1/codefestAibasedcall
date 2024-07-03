package com.sr.capital.service.impl;

import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.primary.User;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.VerificationService;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class VerificationServiceImpl implements VerificationService {

    final RequestValidationStrategy requestValidationStrategy;
    final VerificationEntityServiceImpl verificationEntityService;
    final UserServiceImpl userService;

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
}
