package com.sr.capital.helpers.enums;

public enum CredentialsType {
    EMAIL("email"),
    MOBILE("mobile"),
    USERNAME("username"),

    UNKNOWN("unknown");

    final String type;

    CredentialsType(String type) { this.type = type; }
}
