package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INVALID_MOBILE;

public class InvalidMobileException extends CustomException {
    public InvalidMobileException() {
        super(INVALID_MOBILE, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}