package com.sr.capital.external.crif.Constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CrifDocumentType {
    PAN("Pan"),
    UID("UID"),
    DRIVING_LICENCE("Driving Licence"),
    VOTER("Voter"),
    PASSPORT("Passport"),
    RATION_CARD("Ration Card"),
    NREGA("Nrega"),
    CKYC("CKYC"),
    OTHER("Other");

    private final String displayName;

    CrifDocumentType(String displayName) {
        this.displayName = displayName;
    }

    public static List<CrifDocumentType> getAllDocumentTypes() {
        return Arrays.asList(CrifDocumentType.values());

    }
}
