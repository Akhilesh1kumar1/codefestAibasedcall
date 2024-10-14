package com.sr.capital.service.impl;

import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.ValidateService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ValidateServiceImpl implements ValidateService {

   final RequestValidationStrategy requestValidationStrategy;
    @Override
    public Boolean validatePan(String value) throws Exception {
        return requestValidationStrategy.validateRequest(value, RequestType.PAN);
    }

    @Override
    public Boolean validateGst(String value) throws Exception {
        return requestValidationStrategy.validateRequest(value,RequestType.GST);
    }


    @Override
    public Boolean validateMobileNumber(ValidateMobileNumberRequestDto validateMobileNumberRequestDto) throws Exception {

        return requestValidationStrategy.validateRequest(validateMobileNumberRequestDto,RequestType.VERIFY_MOBILE_FOR_LOAN);

    }
}
