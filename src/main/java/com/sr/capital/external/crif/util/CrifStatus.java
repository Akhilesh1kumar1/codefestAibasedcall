package com.sr.capital.external.crif.util;

public enum CrifStatus {

    CRIF_DETAILS_VERIFICATION("crif_details_verification"),
    CRIF_IDENTITY_VERIFICATION("crif_identity_verification"),
    CRIF_CREDIT_REPORT("crif_credit_report"),
    CRIF_CONSENT_REVOKE("crif_consent_revoke")
    ;
    private final String description;

    CrifStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static CrifStatus from(String code ) {
        for (CrifStatus status : values()) {
            if (status.name().equalsIgnoreCase(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status : " + code);
    }
}