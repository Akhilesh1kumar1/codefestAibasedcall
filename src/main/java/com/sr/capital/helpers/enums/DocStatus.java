package com.sr.capital.helpers.enums;

public enum DocStatus {

    VERIFIED("verified"),
    UNVERIFIED("unverified");

    final String status;

    DocStatus(String status) {
        this.status = status;
    }

    public String getTaskStatus() {
        return this.status;
    }
}
