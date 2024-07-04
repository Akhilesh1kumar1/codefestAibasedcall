package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class InvalidEmailUsageException extends CustomException {
    public InvalidEmailUsageException() {
        super("The entered email is associated with a non-admin account on Shiprocket! Please log in with an admin account or choose a different email.", HttpStatus.FORBIDDEN);
    }
}
