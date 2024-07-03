package com.sr.capital.external.karza.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

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
    public class LendingAmount {
        private Double min = null;
        private Double max = null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Last15MnthSmry {
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
    public class Gstr4a {
        ArrayList<Object> details;

        Object total;
    }


    @Data
    public class Gstr4 {

        ArrayList<Object> details;

        Object total;

    }

    @Data
    public class Gstr3b {

        ArrayList<Object> details;

        Object total;
    }

    @Data
    public class Gstr2a {
        List<GstDetails> details;
        TotalGstDetails total;
    }

    @Data
    public class Gstr1 {
        List<GstDetails> details;
        TotalGstDetails total;
    }

    @Data
    public class GstDetails{
        String retPrd;

        ArrayList<Object> secSum;
    }

    public class TotalGstDetails{
        ArrayList<Object> secSum;
    }
    @Data
    public class Gst1SecSum {
        private String secNm;
        private String chksum;
        private int ttlRec;
        private double ttlTax;
        private double ttlIgst;
        private double ttlSgst;
        private double ttlCgst;
        private double ttlVal;
        private double ttlCess;
        private Double ttlExptAmt= 0.0;
        private Double ttlNgsupAmt= 0.0;
        private Double ttlNilsupAmt=0.0;
        private List<InvoiceDetails> cptySum;
    }

    public class InvoiceDetails {
        private String ctin;
        private String chksum;
        private int ttlRec;
        private double ttlTax;
        private double ttlIgst;
        private double ttlSgst;
        private double ttlCgst;
        private double ttlVal;
        private double ttlCess;
    }
        @Data
    public class TotalGst1SecSum extends Gst1SecSum {
        private String sectionDescription;
        private Object tax; // Assuming 'tax' can be of any type, hence Object type

    }

    public class LastSixMnthSmry {
        Gst gstr1;
        Gst gstr2a;
        Gst gstr3b;
        Gst gstr4;
        Gst gstr4a;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Current {
        List<FillingStatus> filingStatus ;
        private String financialYear;
        private String financialPeriod = null;
        List<FillingFrequency> filingFrequency ;
        ItcCredit itcCredit;
        Gstr1 sstr1;
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
    public class Cagr {
        private Double salesGrowthRate;
        private Double purchasesGrowthRate;
        List<RecurringSalesCpty> recurringSalesCpty ;
        List<RecurringPurchaseCpty> recurringPurchaseCpty ;
        private Double recurringCustPercent;
        private Double recurringSuppPercent;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class RecurringSalesCpty{
        Double minSale;
        String pan;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class RecurringPurchaseCpty{
        Double minPurchase;
        String pan;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class IntraOrgTransactions {
        private Double sales;
        private Double purchases;
        private Double salesPercent;
        private Double purchasesPercent;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ComplianceCheck {
        private Double potentialShortPaymentVal;
        private Boolean potentialShortPayment;
        private Double potentialExcessCreditVal;
        private Boolean potentialExcessCredit;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TurnoverAndCustomers {
        private Double grossTurnover;
        private Double netTurnover;
        private Double ttlInv;
        private Double ttlCustomer;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Total {
        TotalSalesCptySum totalSalesCptySum;
        TotalSalesCptySum totalPurchasesCptySum;
        TotalSalesCptySum totalSalesStateWise;
        TotalSalesCptySum totalPurchasesStateWise;
        TotalSalesCptySum totalSalesHsnWise;
        TotalSalesCptySum totalPurchasesHsnWise;
        TotalSalesCptySum totalSalesCategoryWise;
        TotalSalesCptySum totalPurchasesCategoryWise;

    }

    public class TotalPurchasesCategoryWise {
        private float ttlRec;
        private float ttlTax;
        private float tax;
        private float ttlVal;

    }

    public class TotalSalesCategoryWise {
        private float ttlRec;
        private float ttlTax;
        private float tax;
        private float ttlVal;

    }

    public class TotalPurchasesHsnWise {
        private String ttlRec = null;
        private String tax = null;
        private String ttlTax = null;
        private String ttlVal = null;

    }

    public class TotalSalesHsnWise {
        private String ttlRec = null;
        private String tax = null;
        private String ttlTax = null;
        private String ttlVal = null;
    }

    public class TotalPurchasesStateWise {
        private float ttlRec;
        private float ttlTax;
        private float tax;
        private float ttlVal;
    }

    public class TotalSalesStateWise {
        private float ttlRec;
        private float ttlTax;
        private float tax;
        private float ttlVal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalPurchasesCptySum {
        private Double ttlRec;
        private Double ttlTax;
        private Double tax;
        private Double ttlVal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalSalesCptySum {
        private Double ttlRec;
        private Double ttlTax;
        private Double tax;
        private Double ttlVal;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class QuarterlySummary {
        TotalQuarterResult total;
        Quarter quarter1;
        Quarter quarter2;
        Quarter quarter3;
        Quarter quarter4;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Gst{
        Double tax;
        Double ttlTax;
        Double ttlVal;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalQuarterResult{
        Gst gstr1;
        Gst gstr2a;
        Gst gstr3b;
        Gst gstr4;
        Gst  gstr4a;
    }

    public class Quarter4 {
        private String periodFrom;
        private String periodTo;
        ArrayList<Object> months = new ArrayList<Object>();
        TotalGstr1 totalGstr1;
        TotalGstr2a totalGstr2a;
        TotalGstr3b totalGstr3b;
        TotalGstr4 totalGstr4;
        TotalGstr4a totalGstr4a;
    }
    public class Quarter3 {
        private String periodFrom;
        private String periodTo;
        ArrayList<Object> months = new ArrayList<Object>();
        TotalGstr1 totalGstr1;
        TotalGstr2a totalGstr2a;
        TotalGstr3b totalGstr3b;
        TotalGstr4 totalGstr4;
        TotalGstr4a totalGstr4a;
    }

    public class TotalGstr4a {
        private Double tax = null;
        private Double ttlTax = null;
        private Double ttlVal = null;

    }

    public class TotalGstr4 {
        private Double tax = null;
        private Double ttlTax = null;
        private Double ttlVal = null;
    }

    public class TotalGstr3b {
        private Double tax = null;
        private Double ttlTax = null;
        private Double ttlVal = null;

    }

    public class TotalGstr2a {
        private float tax;
        private float ttlTax;
        private float ttlVal;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalGstr1 {
        private Double tax = null;
        private Double ttlTax = null;
        private Double ttlVal = null;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalGstr {
        private Double tax = null;
        private Double ttlTax = null;
        private Double ttlVal = null;
    }

    public class Quarter {
        private String periodFrom;
        private String periodTo;
        ArrayList<Object> months = new ArrayList<Object>();
        TotalGstr totalGstr1;
        TotalGstr totalGstr2a;
        TotalGstr totalGstr3b;
        TotalGstr totalGstr4;
        TotalGstr totalGstr4a;
    }

    public class Quarter1 {
        private String periodFrom;
        private String periodTo;
        ArrayList<Object> months = new ArrayList<Object>();
        TotalGstr1 totalGstr1;
        TotalGstr2a totalGstr2a;
        TotalGstr3b totalGstr3b;
        TotalGstr4 totalGstr4;
        TotalGstr4a totalGstr4a;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class BusinessSummary {
        private Double netTurnoverLy;
        private Double gstTurnoverCyInvVal;
        private Double gstTurnoverCyTaxVal;
        private Double liabilityPayable;
        private Double totalInvoices;
        private Double liabilityPaid;
        private Double balancePayable;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class MissingData {
        ArrayList<Object> returns = new ArrayList<Object>();
        ArrayList<Object> itc = new ArrayList<Object>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Averages {
        private Double avginvcust;
        private Double avgmonthtax;
        private Double avgmonthval;
        private Double avgttltaxinv;
        private Double avgttlvalinv;
        private Double avgttltaxcust;
        private Double avgttlvalcust;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TransactionSummary {
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
    public class Turnover {
        private Double ttlTax;
        private Double ttlVal;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class TotalTaxLiability {
        private Double camt;
        private Double csamt;
        private Double samt;
        private Double iamt;
        private Double total;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Payment {
        private float ttCshPd;
        private Double ttPay = null;
        private float ttItcPd;
        private float total;
    }

    public class Gstr4aTotal {
        ArrayList<Object> details = new ArrayList<Object>();
        TotalGst4a total;

    }

    public class TotalGst4a {
        ArrayList<Object> secSum = new ArrayList<Object>();
        ArrayList<Object> cptyWise = new ArrayList<Object>();
        ArrayList<Object> stateWise = new ArrayList<Object>();
        ArrayList<Object> hsnWise = new ArrayList<Object>();

    }

    public class Gstr4Total {
        ArrayList<Object> details = new ArrayList<Object>();
        TotalGstr4Total total;
    }
    public class TotalGstr4Total {
    ArrayList<Object> secSum = new ArrayList<Object>();
    ArrayList<Object> cptySum = new ArrayList<Object>();
    ArrayList<Object> stateWise = new ArrayList<Object>();
    ArrayList<Object> hsnWise = new ArrayList<Object>();
    Dbtdtl DbtdtlObject;
    LiabdtlRev LiabdtlRevObject;
    LiabdtlNonrev LiabdtlNonrevObject;
    TotalLiabilityPayable TotalLiabilityPayableObject;
    private String totalLiabilityFrmSecSum = null;
}
    public class TotalLiabilityPayable {
        Sgst sgst;
        Cgst cgst;
        Igst igst;
        Cess cess;
    }

    public class Cess {
        private String oth = null;
        private String fee = null;
        private String tx = null;
        private String tot = null;
        private String pen = null;
        private String intr = null;

    }

    public class Igst {
        private String oth = null;
        private String fee = null;
        private String tx = null;
        private String tot = null;
        private String pen = null;
        private String intr = null;
    }

    public class Cgst {
        private String oth = null;
        private String fee = null;
        private String tx = null;
        private String tot = null;
        private String pen = null;
        private String intr = null;
    }

    public class Sgst {
        private String oth = null;
        private String fee = null;
        private String tx = null;
        private String tot = null;
        private String pen = null;
        private String intr = null;

    }

    public class LiabdtlNonrev {
        Sgst sgst;
        Cgst cgst;
        Igst igst;
        Cess cess;
    }

    public class LiabdtlRev {
        Sgst sgst;
        Cgst cgst;
        Igst igst;
        Cess cess;
    }

    public class Dbtdtl {
        Sgst sgst;
        Cgst cgst;
        Igst igst;
        Cess cess;
    }

    public class Gstr3bTotal {
        ArrayList<Object> details = new ArrayList<Object>();
        TotalGstr3bTotal total;
    }

    class TotalGstr3bTotal {
        SupDetails supDetails;
        IntrLtfee intrLtfee;
        TtVal ttVal;
        ItcElg itcElg;
        InterSup interSup;
        private float itcAvlTotal;
        private float itcRevTotal;
        private float itcNetTotal;
        private float itcInelgTotal;
        private float ttlTaxPayable;
        private float itcAvlByGstr2a;
    }

    public class InterSup {
        ArrayList<Object> unregDetails = new ArrayList<Object>();
        ArrayList<Object> compSummary = new ArrayList<Object>();
        ArrayList<Object> uinSummary = new ArrayList<Object>();
        ArrayList<Object> uinDetails = new ArrayList<Object>();
        ArrayList<Object> compDetails = new ArrayList<Object>();
    }

    public class ItcElg {
        ArrayList<Object> itcAvl = new ArrayList<Object>();
        ArrayList<Object> itcRev = new ArrayList<Object>();
        ItcNet itcNet;
        ArrayList<Object> itcInelg = new ArrayList<Object>();
        private float itcAvlTotal;
        private float itcRevTotal;
        private String itcInelgTotal = null;
    }

    public class ItcNet {
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private float total;

    }

    public class TtVal {
        private float ttCshPd;
        private float ttItcPd;
        private float total;
    }

    public class IntrLtfee {
        IntrDetails intrDetails;
        LtfeeDetails ltfeeDetails;

    }

    public class LtfeeDetails {
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private float total;
    }

    public class IntrDetails {
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private float total;
    }

    public class SupDetails {
        OsupDet osupDet;
        OsupZero osupZero;
        OsupNilExmp osupNilExmp;
        IsupRev isupRev;
        OsupNongst osupNongst;
    }

    public class OsupNongst {
        private float txval;
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private String total = null;
    }

    public class IsupRev {
        private float txval;
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private String total = null;
    }

    public class OsupNilExmp {
        private float txval;
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private String total = null;

    }

    public class OsupZero {
        private float txval;
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private String total = null;
    }

    public class OsupDet {
        private float txval;
        private float iamt;
        private float camt;
        private float samt;
        private float csamt;
        private float total;
    }

    public class Gstr2aTotal {
        ArrayList<Object> details = new ArrayList<Object>();
        TotalGstr2aState total;
    }

    public class TotalGstr2aState {
        ArrayList<Object> secSum = new ArrayList<Object>();
        ArrayList<Object> cptyWise = new ArrayList<Object>();
        ArrayList<Object> stateWise = new ArrayList<Object>();
        ArrayList<Object> hsnWise = new ArrayList<Object>();
    }

    public class Gstr1Total {
        ArrayList<Object> details = new ArrayList<Object>();
        TotalGst1 total;
    }

    public class TotalGst1 {
        ArrayList<Object> secSum = new ArrayList<Object>();
        ArrayList<Object> cptySum = new ArrayList<Object>();
        ArrayList<Object> stateWise = new ArrayList<Object>();
        ArrayList<Object> hsnWise = new ArrayList<Object>();
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class ItcCredit {
        List<ItcCreditDetails> details ;
        Object tranCredit;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  class ItcCreditDetails {

        Object provCrdBalList;
        List<ITCLedgerDetails> itcLdgDtls;


        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class ITCLedgerDetails {
            private String frDt;
            private String toDt;
            private ClosingBalance clBal;
            private String gstin;
            private List<Transaction> tr;


            // Getters and Setters (optional, not included as per your request)
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class ClosingBalance {
                private int sgstTaxBal;
                private int igstTaxBal;
                private int cgstTaxBal;
                private int cessTaxBal;
                private int totRngBal;
                private String desc;

                // Getters and Setters (optional, not included as per your request)
            }
            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Transaction {
                private int cessTaxAmt;
                private int igstTaxBal;
                private int cgstTaxBal;
                private String refNo;
                private int cgstTaxAmt;
                private int totRngBal;
                private String trTyp;
                private String dt;
                private String retPeriod;
                private int sgstTaxBal;
                private int sgstTaxAmt;
                private int igstTaxAmt;
                private int cessTaxBal;
                private int totTrAmt;
                private String desc;

                // Getters and Setters (optional, not included as per your request)
            }
        }

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Previous {
        List<FillingStatus> filingStatus ;
        private String financialYear;
        private String financialPeriod = null;
        List<FillingFrequency> filingFrequency ;
        ItcCredit itcCredit;
        Gstr1 sstr1;
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
    public class FillingFrequency{
        String startPeriod;
        String endPeriod;
        String filingFrequency;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
   public class FillingStatus {
        String retPeriod;
        List<FillingStatusDetails> status;
   }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
   public class FillingStatusDetails{
       private String valid;
       private String mof;
       private String dof;
       private String retPrd;
       private String rtntype;
       private String arn;
       private String status;
       private String dueDt;
       private boolean isDelay;
       private String returnTy;
       private int delayDays;
   }
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

    public class Vintage {
        private String dateOfRegistration;
        private String age;
        private String source;
        private String registrationNo;
    }
    public class Status {
        private boolean isActive;

    }

    public class Contacted {
        private String email;
        private String mobNum;
        private String name;
    }
    public class Pradr {
        private String addr;
        private String ntr;
        private String adr;
        private String em;
        private String lastUpdatedDate;
        private String mb;
    }

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
