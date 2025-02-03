package com.sr.capital.excelprocessor.util;

import java.util.Set;

public class LoanDetailsConstants {

    public static final String SHIPROCKET_APPLICATION_ID = "shiprocket application id";
    public static final String DATE_OF_INITIATION = "date of initiation";
    public static final String LOAN_TYPE = "loan type ( rbf / pre-approved / term loan / credit line )";
    public static final String COMPANY_ID = "company id";
    public static final String COMPANY_NAME = "company name";
    public static final String COMPANY_TIER = "company tier";
    public static final String VENDOR_LOAN_ID = "vendor loan id";
    public static final String LOAN_VENDOR_NAME = "loan vendor name";
    public static final String SHIPROCKET_LOAN_STATUS = "shiprocket loan status";
    public static final String VENDOR_LOAN_STATUS = "vendor loan status";
    public static final String SANCTION_DATE = "sanction date";
    public static final String DISBURSEMENT_DATE = "disbursement date";
    public static final String SANCTION_AMOUNT = "sanction amount";
    public static final String SANCTION_LOAN_TENURE = "sanction loan tenure";
    public static final String SANCTION_LOAN_ROI = "sanction loan roi";
    public static final String DISBURSEMENT_AMOUNT = "disbursement amount";
    public static final String DISBURSEMENT_LOAN_TENURE = "disbursement loan tenure";
    public static final String DISBURSEMENT_LOAN_ROI = "disbursement loan roi";
    public static final String TOTAL_RECOVERABLE_AMOUNT = "total recoverable amount";
    public static final String MONTHLY_EMI_AMOUNT = "monthly emi amount";
    public static final String NEXT_EMI_DATE = "next emi date";
    public static final String LAST_EMI_DATE = "last emi date";
    public static final String TOTAL_REPAYMENT_AMOUNT_RECEIVED = "total repayment amount received";
    public static final String AUM_COLLECTION = "aum collection";
    public static final String PRINCIPAL_OUTSTANDING = "principal outstanding";
    public static final String INTEREST_OUTSTANDING = "interest outstanding";
    public static final String TOTAL_PRINCIPAL_PAID = "total principal paid";
    public static final String TOTAL_INTEREST_PAID = "total interest paid";
    public static final String TOTAL_LPI_PAID = "total lpi paid";
    public static final String TOTAL_BOUNCE_PAID = "total bounce paid";
    public static final String TOTAL_PAID_AMOUNT = "total paid amount";
    public static final String PRINCIPAL_OVERDUE = "principal overdue";
    public static final String INTEREST_OVERDUE = "interest overdue";
    public static final String LPI_OVERDUE = "lpi overdue";
    public static final String BOUNCE_OVERDUE = "bounce overdue";
    public static final String TOTAL_OVERDUE_AMOUNT = "total overdue amount";
    public static final String DPD = "dpd";
    public static final String DPD_BUCKET = "dpd_bucket";
    public static final String AMOUNT_COLLECTED_LAST_30_DAYS = "amount collected last 30 days";
    public static final String AMOUNT_COLLECTED_LAST_60_DAYS = "amount collected last 60 days";
    public static final String REPAYMENT_MTD = "repayment mtd";
    public static final String AMOUNT_DUE_CURR_MONTH = "amount due curr month";
    public static final String EXCESS_PAYMENT = "excess payment";
    public static final String LOAN_STATUS = "loan status";
    public static final String WAIVER_AMOUNT = "waiver amount";
    public static final String AMOUNT_DUE_TILL_DATE = "amount due till date";
    public static final String PROGRAM_CODE = "program code";
    public static final String CITY = "city";
    public static final String STATE = "state";
    public static final String PINCODE = "pincode";
    public static final String PROCESSING_FEE_RATE = "processing fee rate(pf)";
    public static final String EVER_30_PLUS = "ever 30 plus";
    public static final String EVER_60_PLUS = "ever 60 plus";
    public static final String EVER_90_PLUS = "ever 90 plus";
    public static final String EVER_180_PLUS = "ever 180 plus";
    public static final String LAST_EMI_PAID_DATE = "last emi paid date";
    public static final String LAST_PAYMENT_MODE = "last payment mode";
    public static final String LAST_PAYMENT_AMOUNT = "last payment amount";
    public static final String FUTURE_EMI_PENDING_COUNT = "future emi pending count";
    public static final String PAN_NUMBER = "pan number";
    public static final String GST_NUMBER = "gst number";
    public static final String UDHYAM = "udhyam";
    public static final String UDHYAM_NUMBER = "udhyam number";
    public static final String LOAN_DURATION = "loan duration";
    public static final String CURRENT_STATUS = "current status";
    public static final String MESSAGE = "message";
    public static final String STATUS = "Status";
    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";



    public static final Set<String> MANDATORY_FIELDS = Set.of(
            "loanType",
            "companyId",
            "companyName",
            "companyTier",
            "loanVendorName",
            "shipRocketLoanStatus",
            "disbursementDate",
            "sanctionAmount",
            "sanctionLoanTenure",
            "sanctionLoanROI",
            "disbursementAmount",
            "disbursementLoanTenure",
            "disbursementLoanROI",
            "monthlyEMIAmount",
            "dpdBucket",
            "city",
            "state",
            "processingFeeRate"
    );

}