package com.sr.capital.validation;

import com.sr.capital.service.RequestValidator;
import org.springframework.stereotype.Service;

@Service
public class AdharValidator implements RequestValidator {
    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        Boolean result =true;
        return (T)result;
    }
}
