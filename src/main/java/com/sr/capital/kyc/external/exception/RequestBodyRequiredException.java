package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class RequestBodyRequiredException extends CustomRuntimeException {
    public RequestBodyRequiredException(){
        super("INVALID REQUEST :: Body is required for the provided HTTP Method!", HttpStatus.BAD_REQUEST);
    }
}
