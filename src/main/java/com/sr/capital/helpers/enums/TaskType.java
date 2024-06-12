package com.sr.capital.helpers.enums;

public enum TaskType {

    PAN("pan"),
    AADHAAR("aadhaar"),
    GST("gst"),
    BANK_DETAILS("bank_details"),
    PAN_GST("pan_gst"),
    PAN_BANK_DETAILS("pan_bank_details"),
    PAN_AADHAAR("pan_aadhaar"),

    UNKNOWN_TYPE("unknown_type");

    final String type;

    TaskType(String type) {
        this.type = type;
    }

    public String getTaskType() {
        return this.type;
    }
}
