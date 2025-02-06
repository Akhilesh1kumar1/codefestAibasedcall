package com.sr.capital.los.utill;

public enum ExternalCallStatusEnum {

    BASIC_DETAILS("BASIC_DETAILS"),
    PAN_COMPREHENSIVE_FETCHED("PERSONAL_DETAILS"),
    PAN_TO_GST_FETCHED("BUSINESS_DETAILS"),
    GST_IN_FETCHED(""),
    CIN_FETCHED(""),
    CORPVEDA_DETAILS_FETCHED("OFFER_GENERATION"),
//    CORPVEDA_DETAILS_FETCHED("OFFER_ACCEPTANCE"),
    NDD_FETCHED(""),
    GST_ANALYTICS_DONE("DOCUMENT_UPLOAD"),
    NET_BANKING_VERIFIED("");

    public String getScreenName() {
        return screenName;
    }

    final String screenName;

    ExternalCallStatusEnum(String screenName) {
        this.screenName = screenName;
    }
}
