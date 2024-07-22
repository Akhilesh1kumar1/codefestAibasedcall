package com.sr.capital.helpers.enums;

public enum LeadStatus {
    NOT_CONNECTED("Not connected"),
    NOT_INTERESTED("Not Interested"),
    INTERESTED_FOLLOW_UP("Interested & Follow Up"),
    INTERESTED_FULLY_DISBURSED("Interested & Fully Disbursed"),
    INTERESTED_REJECTED("Interested & Rejected"),
    INTERESTED_SANCTIONED("Interested & Sanctioned"),
    INTERESTED_SANCTIONED_NOT_DISBURSED("Interested & Sanctioned but Not Disbursed"),
    INTERESTED_SANCTIONED_NOT_FULLY_DISBURSED("Interested & Sanctioned but Not Fully Disbursed"),
    INTERESTED_FULLY_DISBURSED_AGAIN("Interested & Fully Disbursed"); // Duplicate removed

    private final String displayName;

    LeadStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static void validateLeadStatus(LeadStatus status) throws IllegalArgumentException {
        switch (status) {
            case NOT_INTERESTED:
            case INTERESTED_FULLY_DISBURSED:
            case INTERESTED_REJECTED:
                throw new IllegalArgumentException("Invalid Lead Status: " + status);
            default:
                // Do nothing if the status is valid
        }
    }
}
