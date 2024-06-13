package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class IncompatibleDetailsException extends CustomException {
    public IncompatibleDetailsException(){
        super("INVALID REQUEST :: The details are not compatible with the document type", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
