package com.sr.capital.helpers.enums;

public enum ServiceName {

    SHIPROCKET("Shiprocket"),
    SINFINI("Sinfini"),
    KALEYRA("Kaleyra"),
    NETCORE("netcore"),
    KLUB("klub"),
    INCRED("incred"),
    INDIFI("indifi"),
    VEDFIN("vedfin"),
    STRPVENURES("stripvenures"),
    VELOCITY("velocity"),
    GETVENTAGE("getventage"),
    KARZA("karza"),
    FLEXI("flexi"),
    CAPTCHA("captcha"),
    CRIF("crif");

    final String name;
    ServiceName(String name) {
        this.name = name;
    }

    public  String getName() {
        return name;
    }
}
