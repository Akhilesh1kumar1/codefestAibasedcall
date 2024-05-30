package com.sr.capital.helpers.enums;

public enum VerificationType {

    OTP("otp"),
    LINK("link");
    final String type;

    VerificationType(String type) {
        this.type = type;
    }
}
