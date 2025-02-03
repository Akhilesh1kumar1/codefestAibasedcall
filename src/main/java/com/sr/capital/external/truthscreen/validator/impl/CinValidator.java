package com.sr.capital.external.truthscreen.validator.impl;

import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.validator.TruthScreenRequestValidator;
import org.springframework.stereotype.Service;

@Service
public class CinValidator implements TruthScreenRequestValidator {
    @Override
    public <T, U> T validateRequest(U request, TruthScreenDocDetails truthScreenDocDetails) throws Exception {
        return null;
    }
}
