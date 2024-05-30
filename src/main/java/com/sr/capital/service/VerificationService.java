package com.sr.capital.service;

import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.helpers.enums.RequestType;

public interface VerificationService {

    Boolean verifyPan(String value , RequestType requestType) throws Exception;

    Boolean verifyOtp(VerifyOtpRequest verifyOtpRequest ) throws Exception;

    Boolean verifyGst(String value ) throws Exception;

}
