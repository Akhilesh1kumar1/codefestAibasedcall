package com.sr.capital.kyc.external.exception;

import com.sr.capital.exception.custom.CustomRuntimeException;
import com.sr.capital.helpers.constants.ErrorConstants;
import org.springframework.http.HttpStatus;

public class IDfyExtractionException extends CustomRuntimeException {

    public IDfyExtractionException(String docType) {
        super(ErrorConstants.DOC_UPLOAD_ERROR_MESSAGE, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
