package com.sr.capital.exception.verification;

import com.sr.capital.exception.custom.CustomException;
import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INVALID_VERIFICATION_LINK;


public class InvalidVerificationLinkException extends CustomException {
    public InvalidVerificationLinkException(){
        super(INVALID_VERIFICATION_LINK, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
