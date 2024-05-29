package com.sr.capital.service.strategy;


import com.sr.capital.exception.custom.RequestValidatorNotFoundException;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.validation.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RequestValidationStrategy {

    final PANValidator panValidator;
    final VerifyOtpRequestValidator verifyOtpRequestValidator;
    final GSTValidator gstValidator;
    final TenantBankRequestValidator tenantBankRequestValidator;
    final VerifyBankDetailsRequestValidator verifyBankDetailsRequestValidator;
    final AdharValidator adharValidator;

    public <T,U> T validateRequest(U request, RequestType type) throws Exception {
        RequestValidator requestValidator;
        switch (type) {
            case PAN -> requestValidator = panValidator;
            case MOBILE -> requestValidator =verifyOtpRequestValidator;
            case GST -> requestValidator = gstValidator;
            case BANK_DETAILS -> requestValidator = tenantBankRequestValidator;
            case VERIFY_BANK_DETAILS -> requestValidator =verifyBankDetailsRequestValidator;
            case ADHAR -> requestValidator = adharValidator;
            default -> {
                throw new RequestValidatorNotFoundException();
            }
        }
        return requestValidator.validateRequest(request);
    }
}
