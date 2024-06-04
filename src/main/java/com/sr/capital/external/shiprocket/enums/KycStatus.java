package com.sr.capital.external.shiprocket.enums;

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

    final int type;

    KycStatus(int type) {
        this.type = type;
    }

}
