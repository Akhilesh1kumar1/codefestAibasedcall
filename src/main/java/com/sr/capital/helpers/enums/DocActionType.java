package com.sr.capital.helpers.enums;


public enum DocActionType {

    UPLOAD("upload"),
    EXTRACT("extract"),
    SAVE("save"),

    UNKNOWN_TYPE("unknown_type");

    final String type;

    DocActionType(String type) {
        this.type = type;
    }

    public String getDocActionType() {
        return this.type;
    }
}
