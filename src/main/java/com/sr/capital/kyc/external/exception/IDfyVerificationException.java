package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class IDfyVerificationException extends CustomRuntimeException {

    public IDfyVerificationException(String docType) {
        super("Unable to verify " + docType, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
