package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.UNSUCCESSFUL_OTP_ATTEMPT;

public class OtpFailedAttemptsLimitException extends CustomException {
    public OtpFailedAttemptsLimitException() {
        super(UNSUCCESSFUL_OTP_ATTEMPT, HttpStatus.FORBIDDEN);
    }
}
