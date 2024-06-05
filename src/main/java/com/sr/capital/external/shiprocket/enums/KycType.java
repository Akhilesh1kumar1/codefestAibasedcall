package com.sr.capital.external.shiprocket.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum KycType {

    COMPANY(1),
    INDIVIDUAL(2),
    PROPRIETORSHIP(3);

    final int value;

    public static KycType fromValue(int value) {
        for (KycType type : values()) {
            if (type.getValue() == value) {
                return type;
            }
        }
        return null;
    }

}
