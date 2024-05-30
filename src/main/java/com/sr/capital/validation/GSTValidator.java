package com.sr.capital.validation;

import com.sr.capital.exception.custom.InvalidGSTException;
import com.sr.capital.exception.verification.InvalidPanException;
import com.sr.capital.service.RequestValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class GSTValidator implements RequestValidator {
    private static final String GST_REGEX = "^([0-2][0-9]|3[0-7])[A-Z]{5}[0-9]{4}[A-Z][1-9A-Z]Z[0-9A-Z]$";

    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        Boolean result= isValidGST(String.valueOf(request));

        if(!result){
            throw new InvalidGSTException();
        }
        return (T)result;
    }

    private  Boolean isValidGST(String gstNumber) {
        Pattern pattern = Pattern.compile(GST_REGEX);
        Matcher matcher = pattern.matcher(gstNumber);
        return matcher.matches();
    }

}
