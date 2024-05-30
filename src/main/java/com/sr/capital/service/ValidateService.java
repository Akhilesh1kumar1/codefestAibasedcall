package com.sr.capital.service;

import com.sr.capital.helpers.enums.RequestType;

public interface ValidateService {

    Boolean validatePan(String value ) throws Exception;


    Boolean validateGst(String value ) throws Exception;

}
