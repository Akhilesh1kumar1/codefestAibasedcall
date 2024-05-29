package com.sr.capital.helpers.enums;

public enum MFAType {
    SMS_AUTHENTICATION("sms_authentication"),
    EMAIL_AUTHENTICATION("email_authentication");

    final String type;

    MFAType(String type) {
        this.type = type;
    }
}
