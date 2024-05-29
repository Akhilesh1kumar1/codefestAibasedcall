package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.EXPIRED_OTP;

public class ExpiredOtpException extends CustomException {
    public ExpiredOtpException(){
        super(EXPIRED_OTP, HttpStatus.GONE);
    }
}
