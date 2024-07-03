package com.sr.capital.helpers.enums;

public enum DocType {

    SELFI("selfi"),
    PAN("pan"),
    AADHAAR("aadhaar"),
    GST("gst"),
    BANK_CHEQUE("bank_cheque"),
    MSME("msme"),
    AGREEMENT("agreement"),
    VERIFY_OTP("verify-otp"),


    GST_BY_PAN("gst_by_pan"),
    GST_BASIC("gst_basic"),
    // Old Doc types

    DRIVING_LICENSE("driving_license"),
    VOTER_ID("voter_id"),
    PROPRIETORSHIP("proprietorship"),
    CIN("cin"),

    UNKNOWN_TYPE("unknown_type"),
    BANK_STATEMENT("bank_statement"),
    BANK_NET_BANKING("bank_net_banking");

    final String type;

    DocType(String type) {
        this.type = type;
    }

    public String getTaskType() {
        return this.type;
    }
}
