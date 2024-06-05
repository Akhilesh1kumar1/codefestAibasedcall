package com.sr.capital.external.shiprocket.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum KycStatus {

    DEFAULT(0),
    PENDING(1),
    VERIFIED(2),
    REJECTED(3),
    REAPPLY(4),
    AADHAR_VERIFIED(5),
    GST_VERIFIED(6),
    AUTO_VERIFIED(7),
    NAMEREJECT(8);

    final int value;

    public static KycStatus fromValue(Integer value) {
        for (KycStatus status : values()) {
            if (status.getValue() == value) {
                return status;
            }
        }
        return null;
    }

}
