package com.sr.capital.exception.custom;

import com.sr.capital.helpers.enums.ServiceName;
import org.springframework.http.HttpStatus;

public class ExternalServiceException extends CustomRuntimeException {

    public ExternalServiceException(ServiceName serviceName, HttpStatus httpStatus){
        super("WEB-CLIENT-ERROR :: " + serviceName + " service failed!", httpStatus);
    }

    public ExternalServiceException(HttpStatus httpStatus){
        super("WEB-CLIENT-ERROR :: External Service Failed!", httpStatus);
    }
}
