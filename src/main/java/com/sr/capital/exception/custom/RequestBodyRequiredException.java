package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class RequestBodyRequiredException extends CustomRuntimeException {
    public RequestBodyRequiredException(){
        super("INVALID REQUEST :: Body is required for the provided HTTP Method!", HttpStatus.BAD_REQUEST);
    }
}
