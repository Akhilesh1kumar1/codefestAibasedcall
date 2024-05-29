package com.sr.capital.validation;

import com.sr.capital.exception.verification.InvalidPanException;
import com.sr.capital.service.RequestValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PANValidator implements RequestValidator {

    private static final String PAN_REGEX = "^[A-Z]{5}[0-9]{4}[A-Z]{1}$";

    // Method to validate PAN number
    private Boolean validatePAN(String pan) {
        // Compile the regex pattern
        Pattern pattern = Pattern.compile(PAN_REGEX);
        // Match the input PAN number with the pattern
        Matcher matcher = pattern.matcher(pan);
        // Return if the PAN number matches the pattern
        return matcher.matches();
    }

    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        Boolean result= validatePAN(String.valueOf(request));

        if(!result){
            throw new InvalidPanException();
        }
        return (T)result;
    }
}

