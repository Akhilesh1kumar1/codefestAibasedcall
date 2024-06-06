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
public enum KycDocumentType {

    AADHAAR_UPLOAD("Aadhar Card", "aadhar"),
    CIN_UPLOAD("Company Incorporation document", "cin"),
    GST_UPLOAD("GST Certificate", "gst"),
    LICENSE_UPLOAD("Driving License", "license"),
    PAN_UPLOAD("Pan Card", "pan"),
    PASSPORT_UPLOAD("Valid Passport", "passport"),
    UDYOG_AADHAR("Udyog aadhaar", "uaadhar"),
    VOTER_UPLOAD("Voter ID Card", "voter");

    final String name;
    final String value;
    static final Map<String, KycDocumentType> NAME_MAP = new HashMap<String, KycDocumentType>();

    static {
        for (KycDocumentType kycDocumentType : values()) {
            NAME_MAP.put(kycDocumentType.getName(), kycDocumentType);
        }
    }

    public static KycDocumentType fromName(String docType) {
        return NAME_MAP.get(docType);
    }

}
