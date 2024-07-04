package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INVALID_EMAIL;

public class InvalidEmailException extends CustomException {
    public InvalidEmailException() {
        super(INVALID_EMAIL, HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
