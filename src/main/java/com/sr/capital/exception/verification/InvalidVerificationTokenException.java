package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INVALID_VERIFICATION_TOKEN;


public class InvalidVerificationTokenException extends CustomException {
    public InvalidVerificationTokenException(){
        super(INVALID_VERIFICATION_TOKEN, HttpStatus.BAD_REQUEST);
    }
}
