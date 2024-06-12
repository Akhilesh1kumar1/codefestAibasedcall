package com.sr.capital.external.shiprocket.enums;

import java.util.HashMap;
import java.util.Map;

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
    static final Map<Integer, KycStatus> VALUE_MAP = new HashMap<Integer, KycStatus>();

    static {
        for (KycStatus status : values()) {
            VALUE_MAP.put(status.getValue(), status);
        }
    }

    public static KycStatus fromValue(Integer value) {
        return VALUE_MAP.get(value);
    }

}
