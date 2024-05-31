package com.sr.capital.helpers.enums;

public enum BankAccountType {

    SAVING("saving"),
    CURRENT("current");

    String type;

    BankAccountType(String type){
        this.type =type;
    }
}
