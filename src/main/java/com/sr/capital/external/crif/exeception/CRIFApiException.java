package com.sr.capital.external.crif.exeception;

public class CRIFApiException extends RuntimeException {
    private final String statusCode;
    private final String errorDetails;

    public CRIFApiException(String message, String statusCode, String errorDetails) {
        super(message);
        this.statusCode = statusCode;
        this.errorDetails = errorDetails;
    }

    public CRIFApiException(String message) {
        super(message);
        this.statusCode = "";
        this.errorDetails = "";
    }

    @Override
    public String toString() {
        return String.format("ApiException: %s (Status Code: %d, Details: %s)", 
                              getMessage(), statusCode, errorDetails);
    }
}
