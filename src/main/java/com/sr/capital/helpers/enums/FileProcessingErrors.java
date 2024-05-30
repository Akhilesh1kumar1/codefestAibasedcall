package com.sr.capital.helpers.enums;

public enum FileProcessingErrors {

    INVALID_FILE("File is invalid."),
    HEADER_SEQUENCE_MISMATCH("Headers are not in proper sequence.");

    private final String errorMessage;

    FileProcessingErrors(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
