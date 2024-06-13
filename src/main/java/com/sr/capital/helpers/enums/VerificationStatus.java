package com.sr.capital.helpers.enums;

public enum VerificationStatus {

    PENDING_FOR_MANUAL_APPROVAL("pending_for_manual_approval"),
    PROCESSING("processing"),
    SUCCESS("success"),
    FAILED("failed");

    final String status;

    VerificationStatus(String status) {
        this.status = status;
    }

    public String getVerificationStatus() {
        return this.status;
    }
}
