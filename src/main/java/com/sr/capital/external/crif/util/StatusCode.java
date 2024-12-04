package com.sr.capital.external.crif.util;

public enum StatusCode {
    S00("Transaction Error in inquiry"),
    S01("User Authentication Successful"),
    S02("User Authentication Failure"),
    S03("User Cancelled the Transaction"),
    S04("Authorization for the report ID: not complete"),
    S05("User Validations Failure"),
    S06("Request is accepted by Bureau"),
    S07("Error in request format"),
    S08("Technical Error"),
    S09("No HIT"),
    S10("Auto Authentication – Confident match from Bureau."),
    S11("Authentication Questionnaire phase"),
    UNAUTHORIZED("Authentication failure");
    private final String description;

    StatusCode(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static StatusCode fromCode(String code) {
        for (StatusCode status : values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}
