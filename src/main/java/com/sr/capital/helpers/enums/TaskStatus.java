package com.sr.capital.helpers.enums;

public enum TaskStatus {

    PENDING_FOR_MANUAL_APPROVAL("pending_for_manual_approval"),
    PROCESSING("processing"),
    SUCCESS("success"),
    FAILED("failed");

    final String status;

    TaskStatus(String status) {
        this.status = status;
    }

    public String getTaskStatus() {
        return this.status;
    }
}
