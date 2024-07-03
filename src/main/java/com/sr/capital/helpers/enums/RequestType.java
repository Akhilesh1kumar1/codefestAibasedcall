package com.sr.capital.helpers.enums;

public enum RequestType {

    PAN("pan"),
    MOBILE("mobile"),
    GST("gst"),
    BANK_DETAILS("bank-details"),
    VERIFY_BANK_DETAILS("verify-bank-details"),

    ADHAR("adhar"),
    COMPANY_KYC("comapny_kyc"),
    ENACH_LINKING("enach_linking"),

    LOAN_APPLICATION("loan_application"),
    UPLOAD_AND_EXTRACT("upload_and_extract"),
    UPLOAD_EXTRACT_SAVE("upload-extract-save"),
    VERIFIED_DOC_DETAILS("verified-doc-details"),

    //DOC DETAILS CONTROLLER
    DOC_DETAILS("fetch-doc-details"),
    UPDATE_DOC_DETAILS("doc-update"),
    VERIFY_MOBILE_OTP("verify-otp");

    final String type;

    RequestType(String type) { this.type = type; }
}
