package com.sr.capital.external.truthscreen.validator.impl;

import com.sr.capital.exception.verification.InvalidPanException;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.validator.TruthScreenRequestValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PanComprehensiveValidator implements TruthScreenRequestValidator {

    private static final String PAN_REGEX = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";

    @Override
    public <T, U> T validateRequest(U request, TruthScreenDocDetails truthScreenDocDetails) throws Exception {
        Boolean result= validatePAN(String.valueOf(request));

        if(!result){
            throw new InvalidPanException();
        }
        return (T)result;
    }

    private Boolean validatePAN(String pan) {
        Pattern pattern = Pattern.compile(PAN_REGEX);
        Matcher matcher = pattern.matcher(pan);
        return matcher.matches();
    }
}
