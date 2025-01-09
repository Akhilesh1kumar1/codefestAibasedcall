package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.ADHAR_NOT_FOUND;

public class CaptchaValidationException extends CustomException {

    public CaptchaValidationException(String msg, HttpStatus status){
        super(msg, status);
    }
}