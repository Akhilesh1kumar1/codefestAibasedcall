package com.sr.capital.external.crif.Constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CrifDocumentType {
    PAN("PAN Card"),
    UID("Aadhaar Card (UID)"),
    DRIVING_LICENCE("Driving Licence"),
    VOTER("Voter ID"),
    PASSPORT("Passport"),
    RATION_CARD("Ration Card"),
    NREGA("NREGA Job Card"),
    CKYC("CKYC (Central KYC)");

    private final String displayName;

    CrifDocumentType(String displayName) {
        this.displayName = displayName;
    }

    public static List<CrifDocumentType> getAllDocumentTypes() {
        return Arrays.asList(CrifDocumentType.values());

    }
}
