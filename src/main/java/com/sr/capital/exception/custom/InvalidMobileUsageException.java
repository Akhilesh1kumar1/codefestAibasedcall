package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidMobileUsageException extends CustomException {

    public InvalidMobileUsageException() {
        super("The mobile is associated to another account! Please use a different one.", HttpStatus.FORBIDDEN);
    }
}
