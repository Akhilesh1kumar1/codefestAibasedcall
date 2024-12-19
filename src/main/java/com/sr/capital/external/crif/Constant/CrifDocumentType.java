package com.sr.capital.external.crif.Constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CrifDocumentType {
    PAN("Pan"),
    DRIVING_LICENCE("Driving Licence"),
    VOTER("voter"),
    PASSPORT("Passport"),
    RATION_CARD("Ration Card"),
    UID("uid"),
    OTHER("other"),
    NREGA("Nrega"),
    CKYC("CKYC");

    private final String displayName;

    CrifDocumentType(String displayName) {
        this.displayName = displayName;
    }

    public static List<CrifDocumentType> getAllDocumentTypes() {
        return Arrays.asList(CrifDocumentType.values());

    }
}
