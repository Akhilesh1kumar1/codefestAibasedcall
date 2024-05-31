package com.sr.capital.helpers.enums;

public enum RequestType {

    PAN("pan"),
    MOBILE("mobile"),
    GST("gst"),
    BANK_DETAILS("bank-details"),
    VERIFY_BANK_DETAILS("verify-bank-details"),

    ADHAR("adhar"),
    COMPANY_KYC("comapny_kyc"),
    ENACH_LINKING("enach_linking");

    final String type;

    RequestType(String type) { this.type = type; }
}
