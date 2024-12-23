package com.sr.capital.external.crif.exeception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CRIFApiLimitExceededException extends Exception {
    private final String statusCode;
    private final String errorDetails;

    public CRIFApiLimitExceededException(String message, String statusCode, String errorDetails) {
        this.statusCode = statusCode;
        this.errorDetails = message + " " + errorDetails;
    }

    public CRIFApiLimitExceededException(String message) {
        this.statusCode = "200";
        this.errorDetails = message;
    }

    @Override
    public String toString() {
        return "External Api failed {" +
                "statusCode : '" + statusCode + '\'' +
                ", errorDetails : '" + errorDetails + '\'' +
                '}';
    }
}
