package com.sr.capital.external.karza.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstConsolidationWebhookRequest {

    private Result result;
    private String refId;
    private String requestId;
    private float statusCode;
    private Long srCompanyId;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {

        Profile profile;
        private String gstin;
        Previous previous;
        Current current;
        LastSixMnthSmry lastSixMnthSmry;
        Last15MnthSmry last15MnthSmry;
        private String reportType;
        ArrayList<Object> alerts = new ArrayList<Object>();
        LendingAmount lendingAmount;
        private String version;
        private String excelDownloadLink;
        private String pdfDownloadLink;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LendingAmount {
        private Double min = null;
        private Double max = null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Last15MnthSmry {
        Gst gstr1;
        Gst gstr2a;
        Gst gstr3b;
        Gst gstr4;
        Gst gstr4a;
        private Double salesPerMonth;
        private Double purchasesPerMonth;
        private Double estimatedMargin;
        private Double percentageMargin;

    }

    @Data
    public static class Gstr4a {
        ArrayList<Object> details;

        Object total;
    }


    @Data
    public static class Gstr4 {

        ArrayList<Object> details;

        Object total;

    }

    @Data
    public static class Gstr3b {

        ArrayList<Object> details;

        Object total;
    }

    @Data
    public static class Gstr2a {
        List<GstDetails> details;
        Object total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gstr1 {
        List<GstDetails> details;
        Object total;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GstDetails{
        String retPrd;

        ArrayList<Object> secSum;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TotalGstDetails{
        ArrayList<Object> secSum;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gst1SecSum {
        private String secNm;
        private String chksum;
        private Integer ttlRec;
        private BigDecimal ttlTax;
        private BigDecimal ttlIgst;
        private BigDecimal ttlSgst;
        private BigDecimal ttlCgst;
        private BigDecimal ttlVal;
        private BigDecimal ttlCess;
        private BigDecimal ttlExptAmt;
        private BigDecimal ttlNgsupAmt;
        private BigDecimal ttlNilsupAmt;
        private List<InvoiceDetails> cptySum;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InvoiceDetails {
        private String ctin;
        private String chksum;
        private Integer ttlRec;
        private BigDecimal ttlTax;
        private BigDecimal ttlIgst;
        private BigDecimal ttlSgst;
        private BigDecimal ttlCgst;
        private BigDecimal ttlVal;
        private BigDecimal ttlCess;
    }
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TotalGst1SecSum extends Gst1SecSum {
        private String sectionDescription;
        private Object tax; // Assuming 'tax' can be of any type, hence Object type

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class LastSixMnthSmry {
        Gst gstr1;
        Gst gstr2a;
        Gst gstr3b;
        Gst gstr4;
        Gst gstr4a;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Current {
        List<FillingStatus> filingStatus ;
        private String financialYear;
        private String financialPeriod = null;
        List<FillingFrequency> filingFrequency ;
        ItcCredit itcCredit;
        Gstr1 gstr1;
        Gstr2a gstr2a;
        Gstr3b gstr3b;
        Gstr4 gstr4;
        Gstr4a gstr4a;
        ArrayList<Object> monthWiseSummary = new ArrayList<Object>();
        TransactionSummary transactionSummary;
        ArrayList<Object> purchasesCategoryWise = new ArrayList<Object>();
        ArrayList<Object> purchasesCptySum = new ArrayList<Object>();
        ArrayList<Object> purchasesStateWise = new ArrayList<Object>();
        ArrayList<Object> purchasesHsnWise = new ArrayList<Object>();
        ArrayList<Object> salesCategoryWise = new ArrayList<Object>();
        ArrayList<Object> salesCptySum = new ArrayList<Object>();
        ArrayList<Object> salesStateWise = new ArrayList<Object>();
        ArrayList<Object> salesHsnWise = new ArrayList<Object>();
        Averages averages;
        ArrayList<Object> top10Cus = new ArrayList<Object>();
        ArrayList<Object> top10Sup = new ArrayList<Object>();
        MissingData missingData;
        BusinessSummary businessSummary;
        QuarterlySummary quarterlySummary;
        Total total;
        TurnoverAndCustomers turnoverAndCustomers;
        ComplianceCheck complianceCheck;
        IntraOrgTransactions intraOrgTransactions;
        ArrayList<Object> cyclicTransactions = new ArrayList<Object>();
        Cagr cagr;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cagr {
        private Double salesGrowthRate;
        private Double purchasesGrowthRate;
        List<RecurringSalesCpty> recurringSalesCpty ;
        List<RecurringPurchaseCpty> recurringPurchaseCpty ;
        private Double recurringCustPercent;
        private Double recurringSuppPercent;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RecurringSalesCpty{
        Double minSale;
        String pan;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class RecurringPurchaseCpty{
        Double minPurchase;
        String pan;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class IntraOrgTransactions {
        private Double sales;
        private Double purchases;
        private Double salesPercent;
        private Double purchasesPercent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ComplianceCheck {
        private Double potentialShortPaymentVal;
        private Boolean potentialShortPayment;
        private Double potentialExcessCreditVal;
        private Boolean potentialExcessCredit;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TurnoverAndCustomers {
        private Double grossTurnover;
        private Double netTurnover;
        private Double ttlInv;
        private Double ttlCustomer;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Total {
        TotalSalesCptySum totalSalesCptySum;
        TotalSalesCptySum totalPurchasesCptySum;
        TotalSalesCptySum totalSalesStateWise;
        TotalSalesCptySum totalPurchasesStateWise;
        TotalSalesCptySum totalSalesHsnWise;
        TotalSalesCptySum totalPurchasesHsnWise;
        TotalSalesCptySum totalSalesCategoryWise;
        TotalSalesCptySum totalPurchasesCategoryWise;

    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TotalPurchasesCptySum {
        private BigDecimal ttlRec;
        private BigDecimal ttlTax;
        private BigDecimal tax;
        private BigDecimal ttlVal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TotalSalesCptySum {
        private BigDecimal ttlRec;
        private BigDecimal ttlTax;
        private BigDecimal tax;
        private BigDecimal ttlVal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class QuarterlySummary {
        TotalQuarterResult total;
        Quarter quarter1;
        Quarter quarter2;
        Quarter quarter3;
        Quarter quarter4;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Gst{
        BigDecimal tax;
        BigDecimal ttlTax;
        BigDecimal ttlVal;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TotalQuarterResult{
        Gst gstr1;
        Gst gstr2a;
        Gst gstr3b;
        Gst gstr4;
        Gst  gstr4a;
    }



    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class TotalGstr {
        private BigDecimal tax = null;
        private BigDecimal ttlTax = null;
        private BigDecimal ttlVal = null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public  static class Quarter {
        private String periodFrom;
        private String periodTo;
        ArrayList<Object> months = new ArrayList<Object>();
        TotalGstr totalGstr1;
        TotalGstr totalGstr2a;
        TotalGstr totalGstr3b;
        TotalGstr totalGstr4;
        TotalGstr totalGstr4a;
    }



    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class BusinessSummary {
        private BigDecimal netTurnoverLy;
        private BigDecimal gstTurnoverCyInvVal;
        private BigDecimal gstTurnoverCyTaxVal;
        private BigDecimal liabilityPayable;
        private BigDecimal totalInvoices;
        private BigDecimal liabilityPaid;
        private BigDecimal balancePayable;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class MissingData {
        ArrayList<Object> returns = new ArrayList<Object>();
        ArrayList<Object> itc = new ArrayList<Object>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Averages {
        private BigDecimal avginvcust;
        private BigDecimal avgmonthtax;
        private BigDecimal avgmonthval;
        private BigDecimal avgttltaxinv;
        private BigDecimal avgttlvalinv;
        private BigDecimal avgttltaxcust;
        private BigDecimal avgttlvalcust;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class TransactionSummary {
        private float payable;
        Payment payment;
        TotalTaxLiability totalTaxLiability;
        private Double ttlIntr;
        private Double lateFee;
        private Double ttlInv;
        Turnover turnover;
        private String periodFrom;
        private String periodTo;
    }

    @Data
    public  static class Turnover {
        private Double ttlTax;
        private Double ttlVal;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class TotalTaxLiability {
        private BigDecimal camt;
        private BigDecimal csamt;
        private BigDecimal samt;
        private BigDecimal iamt;
        private BigDecimal total;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Payment {
        private BigDecimal ttCshPd;
        private BigDecimal ttPay = null;
        private BigDecimal ttItcPd;
        private BigDecimal total;
    }




    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class ItcCredit {
        List<ItcCreditDetails> details ;
        Object tranCredit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItcCreditDetails {

        Object provCrdBalList;
        ITCLedgerDetails itcLdgDtls;


        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ITCLedgerDetails {
            private String frDt;
            private String toDt;
            private ClosingBalance clBal;
            private String gstin;
            private List<Transaction> tr;
            private Object opBal;


            // Getters and Setters (optional, not included as per your request)
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ClosingBalance {
                private BigDecimal sgstTaxBal;
                private BigDecimal igstTaxBal;
                private BigDecimal cgstTaxBal;
                private BigDecimal cessTaxBal;
                private BigDecimal totRngBal;
                private String desc;

                // Getters and Setters (optional, not included as per your request)
            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Transaction {
                private BigDecimal cessTaxAmt;
                private BigDecimal igstTaxBal;
                private BigDecimal cgstTaxBal;
                private String refNo;
                private BigDecimal cgstTaxAmt;
                private BigDecimal totRngBal;
                private String trTyp;
                private String dt;
                private String retPeriod;
                private BigDecimal sgstTaxBal;
                private BigDecimal sgstTaxAmt;
                private BigDecimal igstTaxAmt;
                private BigDecimal cessTaxBal;
                private BigDecimal totTrAmt;
                private String desc;

                // Getters and Setters (optional, not included as per your request)
            }
        }

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Previous {
        List<FillingStatus> filingStatus ;
        private String financialYear;
        private String financialPeriod = null;
        List<FillingFrequency> filingFrequency ;
        ItcCredit itcCredit;
        Gstr1 gstr1;
        Gstr2a gstr2a;
        Gstr3b gstr3b;
        Gstr4 gstr4;
        Gstr4a gstr4a;
        ArrayList<Object> monthWiseSummary = new ArrayList<Object>();
        TransactionSummary transactionSummary;
        ArrayList<Object> purchasesCategoryWise = new ArrayList<Object>();
        ArrayList<Object> purchasesCptySum = new ArrayList<Object>();
        ArrayList<Object> purchasesStateWise = new ArrayList<Object>();
        ArrayList<Object> purchasesHsnWise = new ArrayList<Object>();
        ArrayList<Object> salesCategoryWise = new ArrayList<Object>();
        ArrayList<Object> salesCptySum = new ArrayList<Object>();
        ArrayList<Object> salesStateWise = new ArrayList<Object>();
        ArrayList<Object> salesHsnWise = new ArrayList<Object>();
        Averages averages;
        ArrayList<Object> top10Cus = new ArrayList<Object>();
        ArrayList<Object> top10Sup = new ArrayList<Object>();
        MissingData missingData;
        BusinessSummary businessSummary;
        QuarterlySummary quarterlySummary;
        Total total;
        TurnoverAndCustomers turnoverAndCustomers;
        ComplianceCheck complianceCheck;
        IntraOrgTransactions intraOrgTransactions;
        ArrayList<Object> cyclicTransactions = new ArrayList<Object>();
        Cagr cagr;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class FillingFrequency{
        String startPeriod;
        String endPeriod;
        String filingFrequency;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
   public  static class FillingStatus {
        String retPeriod;
        List<FillingStatusDetails> status;
   }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
   public  static class FillingStatusDetails{
       private String valid;
       private String mof;
       private String dof;
       private String retPrd;
       private String rtntype;
       private String arn;
       private String status;
       private String dueDt;
       private Boolean isDelay;
       private String returnTy;
       private Integer delayDays;
   }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile{
            private String stjCd;
            private String stj;
            private String lgnm;
            private String dty;
            ArrayList < Object > adadr = new ArrayList < Object > ();
            private String cxdt;
            ArrayList < Object > nba = new ArrayList < Object > ();
            private String lstupdt;
            private String rgdt;
            private String ctb;
            Pradr pradr;
            private String sts;
            private String tradeNam;
            private String ctjCd;
            private String ctj;
            ArrayList < Object > mbr = new ArrayList < Object > ();
            private String canFlag;
            private String cmpRt;
            Contacted contacted;
            private String ppr;
            private String address;
            ArrayList < Object > documentDetail = new ArrayList < Object > ();
            private String isFieldVisitConducted;
            AddressDetails addressDetails;
            private boolean isDefaulter;
            Status status;
            Vintage vintage;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Vintage {
        private String dateOfRegistration;
        private String age;
        private String source;
        private String registrationNo;
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Status {
        private Boolean isActive;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Contacted {
        private String email;
        private String mobNum;
        private String name;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static class Pradr {
        private String addr;
        private String ntr;
        private String adr;
        private String em;
        private String lastUpdatedDate;
        private String mb;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AddressDetails {
        // Fields
           private String building;
            private String careOf;
            private String complex;
            private String district;
            private String floor;
            private String house;
            private float pin;
            private String state;
            private String street;
            private String untagged;
            private String locality;
    }
}
