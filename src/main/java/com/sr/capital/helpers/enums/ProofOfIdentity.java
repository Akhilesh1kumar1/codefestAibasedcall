package com.sr.capital.helpers.enums;

public enum ProofOfIdentity {

    DRIVING_LICENCE("driving_licence"),
    PAN_CARD("pan_card"),
    AADHAAR("aadhaar"),
    GST("gst"),
    BANK_CHEQUE("bank_cheque"),
    MSME("msme"),
    AGREEMENT("agreement"),



    DRIVING_LICENSE("driving_license"),
    VOTER_ID("voter_id"),
    PROPRIETORSHIP("proprietorship"),
    CIN("cin"),

    UNKNOWN_TYPE("unknown_type");

    final String type;

    ProofOfIdentity(String type) {
        this.type = type;
    }
}
