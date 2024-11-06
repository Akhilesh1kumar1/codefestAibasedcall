package com.sr.capital.service;

import com.sr.capital.dto.request.ValidateMobileNumberRequestDto;

public interface ValidateService {

    Boolean validatePan(String value ) throws Exception;


    Boolean validateGst(String value ) throws Exception;


    Boolean validateMobileNumberAndCreateLoanApplication(ValidateMobileNumberRequestDto validateMobileNumberRequestDto) throws Exception;

}
