package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

import static com.sr.capital.helpers.constants.Constants.MessageConstants.INVALID_GST;

public class InvalidGSTException extends CustomException{

    public InvalidGSTException() {
        super(INVALID_GST, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
