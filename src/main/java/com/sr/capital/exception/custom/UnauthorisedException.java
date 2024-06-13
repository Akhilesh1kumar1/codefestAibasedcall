package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class UnauthorisedException extends CustomException {
    public UnauthorisedException(){
        super("UNAUTHORISED :: You're not authorised to perform this action!", HttpStatus.UNAUTHORIZED);
    }
}
