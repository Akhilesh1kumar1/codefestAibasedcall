package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class RequestValidatorNotFoundException extends CustomException {
    public RequestValidatorNotFoundException() {
        super("Request Validator not found!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
