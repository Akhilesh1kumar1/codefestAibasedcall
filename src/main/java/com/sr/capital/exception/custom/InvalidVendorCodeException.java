package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidVendorCodeException extends CustomException{

    public InvalidVendorCodeException(){
        super("Invalid vendor code", HttpStatus.UNAUTHORIZED);
    }
}
