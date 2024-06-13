package com.sr.capital.exception.custom;

import org.springframework.http.HttpStatus;

public class VerificationInProgressException extends CustomException {
    public VerificationInProgressException(){
        super("KYC is in progress. Please wait for it to complete or try again later!", HttpStatus.FORBIDDEN);
    }
}
