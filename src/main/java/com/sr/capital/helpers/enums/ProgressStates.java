package com.sr.capital.helpers.enums;

import lombok.Getter;

@Getter
public enum ProgressStates {

    SUCCESS("SUCCESS"),
    PARTIAL_SUCCESS("PARTIAL SUCCESS"),
    IN_PROGRESS("IN PROGRESS"),
    FAILED("FAILED");

    private final String progressValue;

    ProgressStates(String progressValue) {
        this.progressValue = progressValue;
    }
}
