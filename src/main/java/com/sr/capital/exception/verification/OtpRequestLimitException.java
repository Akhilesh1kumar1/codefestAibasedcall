package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.OTP_REQUEST_LIMIT;

public class OtpRequestLimitException extends CustomException {
    public OtpRequestLimitException() {
        super(OTP_REQUEST_LIMIT, HttpStatus.TOO_MANY_REQUESTS);
    }
}
