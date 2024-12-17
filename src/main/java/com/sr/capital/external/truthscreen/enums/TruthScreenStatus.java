package com.sr.capital.external.truthscreen.enums;

public enum TruthScreenStatus {

    VERIFIED("verified"),
    UNVERIFIED("unverified");

    private final String name;

    TruthScreenStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
