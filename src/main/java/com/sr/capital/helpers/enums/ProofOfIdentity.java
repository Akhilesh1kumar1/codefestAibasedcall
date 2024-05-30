package com.sr.capital.helpers.enums;

public enum ProofOfIdentity {

    DRIVING_LICENCE("driving_licence"),
    PAN_CARD("pan_card");

    final String type;

    ProofOfIdentity(String type) {
        this.type = type;
    }
}
