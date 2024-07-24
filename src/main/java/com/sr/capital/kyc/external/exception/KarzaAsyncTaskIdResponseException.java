package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class KarzaAsyncTaskIdResponseException extends CustomRuntimeException {

    public KarzaAsyncTaskIdResponseException(String requestId) {
        super("Unable to fetch verification response for requestId: " + requestId, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
