package com.sr.capital.external.corpveda.enums;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum CorpVedaDocType {

    CIN_SEARCH_PLACE_ORDER("cin_search_place_order", 1),
    CIN_SEARCH_GET_DATA("cin_search_get_data", 2);

    private final String name;
    private final int value;

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }


    public static CorpVedaDocType fromValue(int value) {
        for (CorpVedaDocType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

}
