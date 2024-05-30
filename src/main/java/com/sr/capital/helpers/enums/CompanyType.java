package com.sr.capital.helpers.enums;

public enum CompanyType {

    COMPANY("company"),
    LLP("Limited Liability Partnership");

    final String type;

    CompanyType(String type) {
        this.type = type;
    }
}
