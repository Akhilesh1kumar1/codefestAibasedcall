package com.sr.capital.helpers.enums;

public enum CompanyType {

    COMPANY("company"),
    LLP("Limited Liability Partnership"),
    SOLE_PROPRIETORSHIP("Sole Proprietorship"),
    PARTNERSHIP("partnership"),
    PUBLIC_LIMITED_COMAPNY("Public Limited Company"),
    PRIVATE_LIMITED_COMPANY("Private Limited Company");

    final String type;

    CompanyType(String type) {
        this.type = type;
    }
}
