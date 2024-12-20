package com.sr.capital.external.truthscreen.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum TruthScreenStatus {

    VERIFIED("verified"),
    UNVERIFIED("unverified");

    private final String name;

    public String getName() {
        return name;
    }

}
