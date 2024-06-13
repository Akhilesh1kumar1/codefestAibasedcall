package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class UnsupportedHttpMethodException extends CustomRuntimeException {
    public UnsupportedHttpMethodException(){
        super("INVALID REQUEST :: Unsupported HTTP Method! Supported Methods: [HEAD, GET, DELETE, POST, PUT, PATCH]"
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
