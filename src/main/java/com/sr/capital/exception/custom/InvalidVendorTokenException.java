package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidVendorTokenException extends CustomException {
    public InvalidVendorTokenException() {
        super("Invalid vendor token", HttpStatus.UNAUTHORIZED);
    }
}
