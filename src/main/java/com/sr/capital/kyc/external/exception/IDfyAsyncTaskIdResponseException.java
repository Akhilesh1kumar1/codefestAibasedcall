package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import org.springframework.http.HttpStatus;

public class IDfyAsyncTaskIdResponseException extends CustomRuntimeException {

    public IDfyAsyncTaskIdResponseException(String requestId) {
        super("Unable to fetch verification response for requestId: " + requestId, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
