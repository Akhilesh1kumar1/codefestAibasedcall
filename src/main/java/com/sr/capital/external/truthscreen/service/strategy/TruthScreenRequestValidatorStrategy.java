package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.custom.RequestValidatorNotFoundException;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.validator.TruthScreenRequestValidator;
import com.sr.capital.external.truthscreen.validator.impl.CinValidator;
import com.sr.capital.external.truthscreen.validator.impl.GstinValidator;
import com.sr.capital.external.truthscreen.validator.impl.PanComprehensiveValidator;
import com.sr.capital.external.truthscreen.validator.impl.PanToGstValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TruthScreenRequestValidatorStrategy {

    final PanComprehensiveValidator panComprehensiveValidator;
    final PanToGstValidator panToGstValidator;
    final GstinValidator gstinValidator;
    final CinValidator cinValidator;

    public <T,U> T validateRequest(U request, TruthScreenDocDetails truthScreenDocDetails, TruthScreenDocType type) throws Exception {
        TruthScreenRequestValidator requestValidator;
        switch (type) {
            case PAN_COMPREHENSIVE -> requestValidator = panComprehensiveValidator;
            case PAN_TO_GST -> requestValidator = panToGstValidator;
            case GSTIN -> requestValidator = gstinValidator;
            case CIN -> requestValidator = cinValidator;
            default -> {
                throw new RequestValidatorNotFoundException();
            }
        }
        return requestValidator.validateRequest(request, truthScreenDocDetails);
    }

}
