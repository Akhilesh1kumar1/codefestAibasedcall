package com.sr.capital.external.truthscreen.validator;


import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;

public interface TruthScreenRequestValidator {

    <T, U> T validateRequest(U request, TruthScreenDocDetails truthScreenDocDetails) throws Exception;

}
