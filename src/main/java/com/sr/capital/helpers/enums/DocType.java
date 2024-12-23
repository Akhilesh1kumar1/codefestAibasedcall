package com.sr.capital.helpers.enums;

public enum DocType {

    SELFI("selfi"),
    PAN("pan"),
    AADHAR("aadhar"),
    GST("gst"),
    BANK_CHEQUE("bank_cheque"),
    MSME("msme"),
    PROVISIONAL("provisional"),
    LOAN_TRACKER("loan_tracker"),
    AGREEMENT("agreement"),
    VERIFY_OTP("verify-otp"),


    GST_BY_PAN("gst_by_pan"),
    GST_BASIC("gst_basic"),
    // Old Doc types

    DRIVING_LICENCE("driving_licence"),
    VOTING_CARD("voting_card"),
    PROPRIETORSHIP("proprietorship"),
    CIN("cin"),

    UNKNOWN_TYPE("unknown_type"),
    BANK_STATEMENT("bank_statement"),
    BANK_NET_BANKING("bank_net_banking"),
    ITR("itr"),
    BUSINESS_ADDRESS("business_address"),
    PERSONAL_ADDRESS("current_address"),

    DIRECTORS("directors"),
    PAN_GUARANTOR("pan_guarantor"),
    PAN_PERSONAL("pan_personal"),
    PASSPORT("passport"),
    ADHAR_GUARANTOR_COAPPLICANT("aadhar_guarantor_coapplicant"),
    GST_REGISTRATION("gst_registration"),
    SHOP_EST_REGISTRATION("shop_est_registration"),
    TRADE_LICENSE("trade_license"),
    FOOD_LICENSE("food_license"),
    DRUG_LICENSE_CERTIFICATE("drug_license_certificate"),
    UDYAM_REGISTRATION("udyam_registration"),
    UDYOG_AADHAAR("udyog_aadhaar"),
    BANK_STATEMENT_CURRENT_6("bank_statement_current_6"),
    ELECTRICITY_COMPANY("electricity_company"),
    SALE_DEED_COMPANY("sale_deed_company"),
    LANDLINE_BILL_3MONTH("landline_bill_3month"),
    PROPERTY_TAX_RECEIPT("property_tax_receipt"),
    RENT_AGREEMENT_COMPANY("rent_agreement_company"),
    FINANCIAL_AUDIT("financial_audit"),
    ITR_RETURNS("itr_returns"),
    GST_RETURNS_6("gst_returns_6"),
    VALID_PARTNERSHIP_DEED("valid_partnership_deed"),
    COMPANY_PAN("company_pan"),
    COMPANY_COI("company_coi"),
    MOA_AOA_COMPANY("moa_aoa_company"),
    LATEST_CA_SHAREHOLDINGS("latest_ca_shareholdings"),
    ELECTRICITY("electricity"),
    PIPED_GAS_BILL("piped_gas_bill"),
    WATER_BILL("water_bill"),
    SALE_DEED("sale_deed"),
    LANDLINE_BILL("landline_bill"),
    EPAN("epan"),
    TRADE_CERTIFICATE("trade_certificate"),
    PAN_FATHER_HUSBAND("pan_father_husband");

    final String type;

    DocType(String type) {
        this.type = type;
    }

    public String getTaskType() {
        return this.type;
    }
}
