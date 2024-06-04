package com.sr.capital.external.shiprocket.enums;

public enum KycType {

    COMPANY(1),
    INDIVIDUAL(2),
    PROPRIETORSHIP(3);

    final int type;

    KycType(int type) {
        this.type = type;
    }

}
