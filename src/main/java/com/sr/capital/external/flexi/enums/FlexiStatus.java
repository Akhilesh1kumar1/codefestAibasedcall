package com.sr.capital.external.flexi.enums;

public enum FlexiStatus {
    NOT_APPROVED("Not approved"),
    IN_PROGRESS("In Progress"),

    APPROVED("Approved"),

    DISBURSED("Disbursed");

    String name;
    FlexiStatus(String name) {
        this.name = name;
    }
}
