package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class RSAKeysNotFoundException extends CustomException {
    public RSAKeysNotFoundException() {
        super("RSA Key Entity not found!", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}