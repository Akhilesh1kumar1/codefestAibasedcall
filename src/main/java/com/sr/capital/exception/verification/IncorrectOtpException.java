package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INCORRECT_OTP;

public class IncorrectOtpException extends CustomException {

    public IncorrectOtpException(){
        super(INCORRECT_OTP, HttpStatus.UNAUTHORIZED);
    }
}
