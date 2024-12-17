package com.sr.capital.external.truthscreen.enums;


public enum TruthScreenState {

    COMPLETED("completed"),
    PENDING("pending");

    private final String name;

    TruthScreenState(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }



}
