package com.sr.capital.external.crif.exeception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CRIFApiException extends Exception {
    private final String statusCode;
    private final String errorDetails;

    public CRIFApiException(String message, String statusCode, String errorDetails) {
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }

    public CRIFApiException(String message) {
        this.statusCode = "200";
        this.errorDetails = "";
    }
}
