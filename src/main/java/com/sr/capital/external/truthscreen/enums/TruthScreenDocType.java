package com.sr.capital.external.truthscreen.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TruthScreenDocType {

        PAN("pan", 2),
        Pan_Compliance("pan_compliance",527),
        PAN_COMPREHENSIVE("pan_comprehensive",523);
        private final String name;
        private final int value;

    public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

    public static TruthScreenDocType fromValue(int value) {
        for (TruthScreenDocType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }

    public static TruthScreenDocType fromName(String name){
        for(TruthScreenDocType type : values()){
            if (type.getName() == name){
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + name);
    }
}
