package com.sr.capital.external.crif.exeception;

public class CRIFApiException extends RuntimeException {
    private final String statusCode;
    private final String errorDetails;

    public CRIFApiException(String message, String statusCode, String errorDetails) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getErrorDetails() {
        return errorDetails;
    }

    @Override
    public String toString() {
        return String.format("ApiException: %s (Status Code: %d, Details: %s)", 
                              getMessage(), statusCode, errorDetails);
    }
}
