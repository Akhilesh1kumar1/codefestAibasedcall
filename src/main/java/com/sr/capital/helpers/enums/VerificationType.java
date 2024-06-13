package com.sr.capital.helpers.enums;

public enum VerificationType {

    OTP("otp"),
    LINK("link"),
    DOC_VERIFICATION("doc_verification"),
    BANK_VERIFICATION("bank_verification");
    final String type;

    VerificationType(String type) {
        this.type = type;
    }
}
