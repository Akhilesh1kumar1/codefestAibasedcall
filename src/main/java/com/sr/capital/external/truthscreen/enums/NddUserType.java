package com.sr.capital.external.truthscreen.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum NddUserType {

    ENTITY("Entity"),
    INDIVIDUAL("Individual");

    String name;

    public String getDisplayName() { // New method to get the display name
        return name;
    }

}
