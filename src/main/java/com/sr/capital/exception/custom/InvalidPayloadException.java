package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidPayloadException extends CustomException {
    public InvalidPayloadException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
