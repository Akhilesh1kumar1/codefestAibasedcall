package com.sr.capital.excelprocessor.util;

import java.util.Set;

public class LoanMandatoryConstants {
    public static final String LOAN_TYPE = "loanType";
    public static final String COMPANY_ID = "companyId";
    public static final String COMPANY_NAME = "companyName";
    public static final String COMPANY_TIER = "companyTier";
    public static final String LOAN_VENDOR_NAME = "loanVendorName";
    public static final String SHIP_ROCKET_LOAN_STATUS = "shipRocketLoanStatus";
    public static final String DISBURSEMENT_DATE = "disbursementDate";
    public static final String SANCTION_AMOUNT = "sanctionAmount";
    public static final String SANCTION_LOAN_TENURE = "sanctionLoanTenure";
    public static final String SANCTION_LOAN_ROI = "sanctionLoanROI";
    public static final String DISBURSEMENT_AMOUNT = "disbursementAmount";
    public static final String DISBURSEMENT_LOAN_TENURE = "disbursementLoanTenure";
    public static final String DISBURSEMENT_LOAN_ROI = "disbursementLoanROI";
    public static final String MONTHLY_EMI_AMOUNT = "monthlyEMIAmount";
    public static final String DPD_BUCKET = "dpdBucket";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PROCESSING_FEE_RATE = "processingFeeRate";

    // Define the set of mandatory fields using the constants
    public static final Set<String> MANDATORY_FIELDS = Set.of(
        LOAN_TYPE,
        COMPANY_ID,
        COMPANY_NAME,
        COMPANY_TIER,
        LOAN_VENDOR_NAME,
        SHIP_ROCKET_LOAN_STATUS,
        DISBURSEMENT_DATE,
        SANCTION_AMOUNT,
        SANCTION_LOAN_TENURE,
        SANCTION_LOAN_ROI,
        DISBURSEMENT_AMOUNT,
        DISBURSEMENT_LOAN_TENURE,
        DISBURSEMENT_LOAN_ROI,
        MONTHLY_EMI_AMOUNT,
        DPD_BUCKET,
        CITY,
        STATE,
        PROCESSING_FEE_RATE
    );
}