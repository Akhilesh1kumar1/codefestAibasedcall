package com.sr.capital.external.flexi.constants;

public enum Checkpoint {
    PERSONAL_DETAILS,
    BANKING_DETAILS,
    BUSINESS_DETAILS,
    BUREAU_PULL,
    PARTNER_OFFER,
    OFFER_GENERATED,
    OFFER_ACCEPTED,
    CKYC_VERIFIED,
    DIGILOCKER_VERIFIED,
    DOCUMENTS_RECEIVED,
    BANKING_RECEIVED,
    DOCUMENTS_VERIFIED,
    LOAN_APPROVED,
    AGREEMENT_SIGNED,
    REPAYMENT_SETUP,
    VKYC_VERIFIED,
    LOAN_DISBURSAL,
    LINE_ACTIVATION,
    SELFIE_VERIFICATION,
    NSDL_VERIFICATION,
    LOAN_SANCTION,
    PHYSICAL_NACH,
    UNDERWRITING,
    SANCTION_ACCEPTED;

    public static boolean isValuePresent(String value) {
        try {
            Checkpoint.valueOf(value); // Throws exception if value is invalid
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
