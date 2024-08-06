package com.sr.capital.kyc.external.response.extraction.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.AccessType;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ItrAdditionalResponseData {


    AIS ais;
    GeneralInformation generalInformation;
    private List<TopCustomer> topCustomers;
    private List<TopSupplier> topSuppliers;
    private List<ItrFilled> itrFilled;
    private List<Object> ubo;
    private List<OutstandingDemand> outstandingDemand;
    private BadDebtDetails badDebtDetails;
    private Object alerts;
    private String excelReportLink;
    private AuditInfo auditInfo;
    private BankingRelations bankingRelations;
    private String pdfDownloadLink;
    private KeyPersonsInfo keyPersons;
    private Object natureOfComp;
    private List<FinancialInfo> financialInformation;
    private String version;

    @JsonProperty("26asData")
    private List<AssessmentYearData> asData;

    private FormDetails formDetails;


    @Data
    public static class AssessmentYearData {
        private String statusOf26asData;
        private String assessmentYear;
        private Data data;

        @lombok.Data
        public static class Data {
            private List<TcsDetails> tcsDetails;
            private List<OthTaxPaid> othTaxPaid;
            private List<AirTransaction> airTransaction;
            private List<Gstr3b> gstr3b;
            private TdsImmv tdsImmv;
            private TdsDetails tdsDetails;
            private List<RefundPaid> refundPaid;
            private List<TdsDefaults> tdsDefaults;

            @lombok.Data
            public static class TcsDetails {
                private List<CollectorWise> collectorWise;
                private List<Summary> summary;

                @lombok.Data

                public static class CollectorWise {
                    private String amountDebited;
                    private String section;
                    private String transactionDate;
                    private String taxCollected;
                    private String remarks;
                    private String nameOfCollector;
                    private String statusOfBooking;
                    private String tan;
                    private String tcsDeposited;
                    private String dateOfBooking;
                }

                @lombok.Data

                public static class Summary {
                    private String nameOfCollector;
                    private String taxCollected;
                    private String tan;
                    private String tcsDeposited;
                    private String amountDebited;
                }
            }

            @lombok.Data

            public static class OthTaxPaid {
                private String surchg;
                private String oth;
                private String bsrCode;
                private String majorHead;
                private String taxCollected;
                private String challanNo;
                private String minorHead;
                private String dtOfDeposit;
                private String totalTax;
                private String remarks;
                private String edCess;
            }

            @lombok.Data

            public static class AirTransaction {
                private String partyTransaction;
                private String nameOfAIRFiler;
                private String noOfParties;
                private String transactionDate;
                private String typeOfTransaction;
                private String amount;
                private String mode;
                private String remarks;
            }

            @lombok.Data

            public static class Gstr3b {
                private String srNo;
                private String retPrd;
                private String dof;
                private String txblTrnovr;
                private String gstin;
                private String ttlTrnovr;
                private String arn;
            }

            @lombok.Data

            public static class TdsImmv {
                private SellerOrLandlord sellerOrLandlord;
                private BuyerOrTenant buyerOrTenant;

                public static class SellerOrLandlord {
                    private List<Record> records;
                    private String ttlDdmndPymnt;
                    private String ttlTdsDepstd;

                    @lombok.Data

                    public static class Record {
                        private List<DeductorWise> deductorWise;
                        private List<Summary> summary;

                        public static class DeductorWise {
                            private String status;
                            private String demdPaymnt;
                            private String dateOfDep;
                            private String tdsCertNo;
                            private String tdsDeposited;
                            private String dateOfBooking;
                            private String nameOfDeductor;
                            private String pan;
                        }

                        @lombok.Data

                        public static class Summary {
                            private String transcAmt;
                            private String ackNo;
                            private String nameOfDeductor;
                            private String tdsDeposited;
                            private String pan;
                            private String transcDate;
                        }
                    }
                }

                @lombok.Data

                public static class BuyerOrTenant {
                    private List<Record> records;
                    private String ttlAmtOthTDS;
                    private String ttlDemdPaymnt;
                    private String ttlTdsDeposited;

                    @lombok.Data
                    public static class Record {
                        private List<DeducteeWise> deducteeWise;
                        private List<Summary> summary;

                        @lombok.Data
                        public static class DeducteeWise {
                            private String status;
                            private String demdPaymnt;
                            private String amtOthTds;
                            private String dateOfDep;
                            private String tdsCertNo;
                            private String tdsDeposited;
                            private String dateOfBooking;
                            private String nameOfDeductee;
                            private String pan;
                        }

                        @lombok.Data
                        public static class Summary {
                            private String transcAmt;
                            private String nameOfDeductee;
                            private String ackNo;
                            private String ttlOthTds;
                            private String tdsDeposited;
                            private String pan;
                            private String transcDate;
                        }
                    }
                }
            }

            @lombok.Data
            public static class TdsDetails {
                private List<Tds> tds;
                private List<Tds15g15h> tds15g15h;

                @lombok.Data
                public static class Tds {
                    private List<DeductorWise> deductorWise;
                    private List<Summary> summary;

                    @lombok.Data
                    public static class DeductorWise {
                        private String section;
                        private String transactionDate;
                        private String amountCredited;
                        private String taxDeducted;
                        private String nameOfDeductor;
                        private String remarks;
                        private String statusOfBooking;
                        private String tan;
                        private String tdsDeposited;
                        private String dateOfBooking;
                    }

                    @lombok.Data
                    public static class Summary {
                        private String tdsDeposited;
                        private String tan;
                        private String taxDeducted;
                        private String amountCredited;
                        private String nameOfDeductor;
                    }
                }

                @lombok.Data
                public static class Tds15g15h {
                    private List<DeductorWise> deductorWise;
                    private List<Summary> summary;

                    public static class DeductorWise {
                        private String section;
                        private String transactionDate;
                        private String amountCredited;
                        private String taxDeducted;
                        private String nameOfDeductor;
                        private String remarks;
                        private String tan;
                        private String tdsDeposited;
                        private String dateOfBooking;
                    }

                    @lombok.Data
                    public static class Summary {
                        private String tdsDeposited;
                        private String tan;
                        private String taxDeducted;
                        private String amountCredited;
                        private String nameOfDeductor;
                    }
                }
            }

            @lombok.Data
            public static class RefundPaid {
                private String natRefnd;
                private String dateOfPay;
                private String refndIssd;
                private String interest;
                private String year;
                private String remarks;
                private String amtRefnd;
                private String mode;
            }

            @lombok.Data
            public static class TdsDefaults {
                private List<DeducteeWise> deductorWise;
                private List<Summary> summary;

                @lombok.Data
                public static class DeducteeWise {
                    private String intTDSPayDef;
                    private String shrtPmt;
                    private String ttlDef;
                    private String intTDSDedDef;
                    private String shrtDed;
                    private String interest;
                    private String tan;
                    private String ltFilingFee;
                }

                @lombok.Data
                public static class Summary {
                    private String intTDSPayDef;
                    private String shrtPmt;
                    private String ttlDef;
                    private String intTDSDedDef;
                    private String shrtDed;
                    private String interest;
                    private String finYear;
                    private String ltFilingFee;
                }
            }
        }
    }

    @lombok.Data

    public static class FinancialInfo {
        private BalanceSheet balanceSheet;
        private IncomeConcentration incomeConcentration;
        private TaxDetails taxDetails;
        private TurnoverDetails turnoverDetails;
        private String assessmentYear;
        private Ratios ratios;
        private ProfitAndLossDt profitAndLossDt;
        private String financialYear;
        private ProfitAndLoss profitAndLoss;
        private BalanceSheetDt balanceSheetDt;
        private QuantitativeDetails quantitativeDetails;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class BalanceSheet {
        private BigDecimal totalCurrentLiability;
        private BigDecimal totalInventory;
        private BigDecimal totalEL;
        private BigDecimal tradeReceivables;
        private BigDecimal totalLiability;
        private BigDecimal tradePayables;
        private BigDecimal totalInvestment;
        private BigDecimal totalAssets;
        private BigDecimal totalFixedAsset;
        private BigDecimal totalEquity;
        private BigDecimal cashAndCashEqv;
        private BigDecimal totalCurrentAsset;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class IncomeConcentration {
        private BigDecimal salary;
        private BigDecimal otherSources;
        private BigDecimal houseProperty;
        private BigDecimal capitalGains;
        private BigDecimal businessProfession;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class TaxDetails {
        private BigDecimal refund;
        private BigDecimal totalSelfAssessmentTaxPaid;
        private BigDecimal totalTaxesPaid;
        private BigDecimal totalTcsClaimed;
        private BigDecimal totalAdvanceTaxPaid;
        private BigDecimal totalTdsClaimed;
        private BigDecimal amountPayable;
        private BigDecimal totalInterestAndFeePayable;
        private BigDecimal netTaxLiability;
        private BigDecimal aggregateLiability;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class TurnoverDetails {
        private BigDecimal nonOperating;
        private BigDecimal operating;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class Ratios {
        private ActivityRatios activityRatios;
        private SolvencyRatios solvencyRatios;
        private ProfitabilityRatios profitabilityRatios;
        private GrowthRatios growthRatios;
        private LiquidityRatios liquidityRatios;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class ActivityRatios {
        private BigDecimal daysOfInventoryOutstanding;
        private BigDecimal receivablesTurnover;
        private BigDecimal cashConversionCycle;
        private BigDecimal payablesTurnover;
        private BigDecimal totalAssetTurnover;
        private BigDecimal daysOfSalesOutstanding;
        private BigDecimal inventoryTurnover;
        private BigDecimal daysOfPayablesOutstanding;
        private BigDecimal workingCapitalTurnover;
        private BigDecimal daysWorkingCapital;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class SolvencyRatios {
        private BigDecimal equityMultiplier;
        private BigDecimal debtCapital;
        private BigDecimal ebitdaCoverage;
        private BigDecimal debtEquity;
        private BigDecimal debtEBITDA;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class ProfitabilityRatios {
        private BigDecimal returnOnEquity;
        private BigDecimal netProfitMargin;
        private BigDecimal operatingProfitMargin;
        private BigDecimal returnOnAssets;
        private BigDecimal grossProfitMargin;
        private BigDecimal ebitdaMargin;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class GrowthRatios {
        private BigDecimal totalLiabilitiesGrowth;
        private BigDecimal netSalesGrowth;
        private BigDecimal netWorthGrowth;
        private BigDecimal totalAssetsGrowth;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class LiquidityRatios {
        private BigDecimal interestCoverage;
        private BigDecimal currentRatio;
        private BigDecimal cashRatio;
        private BigDecimal quickRatio;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class ProfitAndLossDt {
        private BigDecimal shareOfProfitOrLossOfAssociatesAndJointVenturesAccountedForUsingEquityMethod;
        private BigDecimal taxExpenseOfDiscontinuedOperations;
        private BigDecimal profitOrLossFromDiscontinuedOperationsBeforeTax;
        private BigDecimal profitBeforeExceptionalItemsAndTax;
        private BigDecimal incomeFromBP;
        private BigDecimal exceptionalItemsBeforeTax;
        private BigDecimal incomeFromOS;
        private BigDecimal grossProfit;
        private BigDecimal totalProfitOrLossForPeriod;
        private BigDecimal totalProfitOrLossFromDiscontinuedOperationsAfterTax;
        private Expenses expenses;
        private Income income;
        private BigDecimal netMovementInRegulatoryDeferralAccountBalancesRelatedToProfitOrLossAndTheRelatedDeferredTaxMovement;
        private BigDecimal incomeFromSalary;
        private BigDecimal totalProfitBeforeTax;
        private BigDecimal incomeFromHP;
        private TaxExpense taxExpense;
        private BigDecimal totalProfitOrLossForPeriodFromContinuingOperations;
        private BigDecimal incomeFromCG;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class Expenses {
        private BigDecimal employeeBenefitExpense;
        private BigDecimal costOfMaterialsConsumed;
        private BigDecimal changesInInventoriesOfFinishedGoodsWorkInProgressAndStockInTrade;
        private BigDecimal expenditureOnProductionTransportationAndOtherExpenditurePertainingToExplorationAndProductionActivities;
        private BigDecimal totalExpenses;
        private BigDecimal otherExpenses;
        private BigDecimal depreciationDepletionAndAmortisationExpense;
        private BigDecimal purchasesOfStockInTrade;
        private BigDecimal financeCosts;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class Income {
        private BigDecimal totalIncome;
        private BigDecimal otherIncome;
        private BigDecimal revenueFromOperations;

        // Add getters, setters, and other methods as needed
    }


    @Data
    public static class TaxExpense {
        private BigDecimal currentTax;
        private BigDecimal totalTaxExpense;
        private BigDecimal deferredTax;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class ProfitAndLoss {
        private BigDecimal purchases;
        private BigDecimal directCost;
        private BigDecimal profitBeforeTax;
        private BigDecimal interestExpense;
        private BigDecimal totalExpense;
        private BigDecimal incomeFromBP;
        private BigDecimal incomeFromOS;
        private BigDecimal profitAfterTax;
        private BigDecimal grossProfit;
        private BigDecimal incomeFromSalary;
        private BigDecimal incomeFromHP;
        private BigDecimal totalTax;
        private BigDecimal revenueFromOperations;
        private BigDecimal ebitda;
        private BigDecimal totalRevenue;
        private BigDecimal incomeFromCG;

        // Add getters, setters, and other methods as needed
    }

    @Data
    public static class BalanceSheetDt {
        private EquityAndLiabilities equityAndLiabilities;
        private EquityAndLiabilities.Assets assets;

        @Data
        public static class EquityAndLiabilities {
            private Liabilities liabilities;
            private BigDecimal totalEquityAndLiabilities;
            private Liabilities.Equity equity;

            @Data
            public static class Liabilities {
                private NonCurrentLiabilities nonCurrentLiabilities;
                private BigDecimal regulatoryDeferralAccountCreditBalancesAndRelatedDeferredTaxLiability;
                private CurrentLiabilities currentLiabilities;
                private BigDecimal totalLiabilities;
                private BigDecimal liabilitiesDirectlyAssociatedWithAssetsInDisposalGroupClassifiedAsHeldForSale;

                @Data
                public static class NonCurrentLiabilities {
                    private BigDecimal deferredGovernmentGrantsNonCurrent;
                    private BigDecimal otherNonCurrentLiabilities;
                    private BigDecimal deferredTaxLiabilitiesNet;
                    private NonCurrentFinancialLiabilities nonCurrentFinancialLiabilities;
                    private BigDecimal totalNonCurrentLiabilities;
                    private BigDecimal provisionsNonCurrent;

                    @Data
                    public static class NonCurrentFinancialLiabilities {
                        private BigDecimal tradePayablesNonCurrent;
                        private BigDecimal otherNonCurrentFinancialLiabilities;
                        private BigDecimal borrowingsNonCurrent;
                        private SecLn secLn;
                        private UnsecLn unsecLn;
                        private BigDecimal totalNonCurrentFinancialLiabilities;

                        @Data
                        public static class SecLn {
                            private BigDecimal ttlSecLn;
                            private BigDecimal rplFromOthers;
                            private BigDecimal frgnCurrLn;
                            private BigDecimal rplFromBanks;
                            private BigDecimal ttlRpLn;
                        }

                        @Data
                        public static class UnsecLn {
                            private BigDecimal ttlUnsecLn;
                            private BigDecimal rplFrRelParty;
                            private BigDecimal rplFrOth;
                            private BigDecimal frgnCurrLn;
                            private BigDecimal rplFrBanks;
                            private BigDecimal ttlRpLn;
                        }
                    }
                }

                @Data
                public static class CurrentLiabilities {
                    private BigDecimal totalCurrentLiabilities;
                    private BigDecimal otherCurrentLiabilities;
                    private BigDecimal deferredGovernmentGrantsCurrent;
                    private BigDecimal currentTaxLiabilities;
                    private BigDecimal provisionsCurrent;
                    private CurrentFinancialLiabilities currentFinancialLiabilities;

                    @Data
                    public static class CurrentFinancialLiabilities {
                        private BigDecimal borrowingsCurrent;
                        private BigDecimal totalCurrentFinancialLiabilities;
                        private BigDecimal tradePayablesCurrent;
                        private BigDecimal otherCurrentFinancialLiabilities;
                    }
                }

                @Data
                public static class Equity {
                    private EquityAttributableToOwnersOfParent equityAttributableToOwnersOfParent;
                    private BigDecimal shareApplicationMoneyPendingAllotment;
                    private BigDecimal totalEquity;
                    private BigDecimal moneyReceivedAgainstShareWarrants;
                    private BigDecimal nonControllingInterest;

                    @Data
                    public static class EquityAttributableToOwnersOfParent {
                        private BigDecimal equityShareCapital;
                        private BigDecimal totalEquityAttributableToOwnersOfParent;
                        private BigDecimal otherEquity;
                    }
                }
            }

            public static class Assets {
                private BigDecimal regulatoryDeferralAccountDebitBalancesAndRelatedDeferredTaxAssets;
                private BigDecimal nonCurrentAssetsClassifiedAsHeldForSale;
                private CurrentAssets currentAssets;
                private BigDecimal totalAssets;
                private NonCurrentAssets nonCurrentAssets;

                public static class CurrentAssets {
                    private CurrentFinancialAssets currentFinancialAssets;
                    private BigDecimal inventories;
                    private BigDecimal totalCurrentAssets;
                    private BigDecimal currentTaxAssets;
                    private BigDecimal otherCurrentAssets;

                    public static class CurrentFinancialAssets {
                        private BigDecimal loansCurrent;
                        private BigDecimal totalCurrentFinancialAssets;
                        private BigDecimal bankBalanceOtherThanCashAndCashEquivalents;
                        private BigDecimal otherCurrentFinancialAssets;
                        private BigDecimal currentInvestments;
                        private BigDecimal cashAndCashEquivalents;
                        private BigDecimal tradeReceivablesCurrent;
                    }
                }

                public static class NonCurrentAssets {
                    private BigDecimal propertyPlantAndEquipment;
                    private BigDecimal biologicalAssetsOtherThanBearerPlants;
                    private BigDecimal goodwill;
                    private BigDecimal intangibleAssetsUnderDevelopment;
                    private BigDecimal otherNonCurrentAssets;
                    private BigDecimal deferredTaxAssetsNet;
                    private BigDecimal investmentsAccountedForUsingEquityMethod;
                    private BigDecimal capitalWorkInProgress;
                    private NonCurrentFinancialAssets nonCurrentFinancialAssets;
                    private BigDecimal totalNonCurrentAssets;
                    private BigDecimal investmentProperty;
                    private BigDecimal otherIntangibleAssets;

                    public static class NonCurrentFinancialAssets {
                        private BigDecimal tradeReceivablesNonCurrent;
                        private BigDecimal loansNonCurrent;
                        private BigDecimal totalNonCurrentFinancialAssets;
                        private BigDecimal otherNonCurrentFinancialAssets;
                        private BigDecimal nonCurrentInvestments;
                    }
                }
            }
        }
    }

    @Data
    public static class QuantitativeDetails {
        private List<TradingConcern> tradingConcern;
        private ManufacturingConcern manufacturingConcern;

        @Data
        public static class TradingConcern {
            private BigDecimal saleQty;
            private Integer unitOfMeasure;
            private BigDecimal purchaseQty;
            private BigDecimal percentYld;
            private BigDecimal yldFinisProd;
            private BigDecimal openingStock;
            private BigDecimal prevyrManfact;
            private BigDecimal clgStock;
            private BigDecimal anyShortExces;
            private String itemName;
            private BigDecimal prevYrConsum;
        }

        @Data
        public static class ManufacturingConcern {
            private List<FinishrByProd> finishrByProd;
            private List<RawMaterial> rawMaterial;

            public static class FinishrByProd {
                private BigDecimal saleQty;
                private String unitOfMeasure;
                private BigDecimal purchaseQty;
                private BigDecimal anyShortExces;
                private BigDecimal openingStock;
                private BigDecimal prevyrManfact;
                private BigDecimal clgStock;
                private String itemName;
            }

            @Data
            public static class RawMaterial {
                private BigDecimal saleQty;
                private BigDecimal percentYld;
                private BigDecimal purchaseQty;
                private BigDecimal unitOfMeasure;
                private BigDecimal yldFinisProd;
                private BigDecimal openingStock;
                private BigDecimal clgStock;
                private BigDecimal anyShortExces;
                private String itemName;
                private BigDecimal prevYrConsum;
            }
        }
    }

        @Data
    public static class KeyPersonsInfo {
        private List<ShareHolderInfo> shareHolderInfo;
        private List<KeyPerson> keyPersons;
    }

    @Data

    public static class ShareHolderInfo {
        private String din;
        private String addressDetailPinCode;
        private String name;
        private String designation;
        private String keyPersonPan;
        private String remunerationPaidPayable;
        private String aadhaarCardNo;
        private String percentageOfShare;
        private String addressDetailCityOrTownOrDistrict;
        private String partnersStatus;
        private String addressDetailStateCode;
        private String addressDetailCountryCode;
        private String addressDetail;
        private String ifMemberAForeignCompany;
        private String rateOfInterestOnCapital;
    }

    @Data
    public static class KeyPerson {
        private String din;
        private String addressDetailPinCode;
        private String name;
        private String designation;
        private String keyPersonPan;
        private String remunerationPaidPayable;
        private String aadhaarCardNo;
        private String percentageOfShare;
        private String partnersStatus;
        private String addressDetailStateCode;
        private String addressDetailCountryCode;
        private String addressDetail;
        private String ifMemberAForeignCompany;
        private String rateOfInterestOnCapital;
    }

    @Data
    public static class BankingRelations {
        private Additional additional;
        private Primary primary;
    }
    @Data
    public static class Additional {
        private List<Domestic> domestic;
        private List<Foreign> foreign;
    }

    @Data
    public static class Domestic {
        private String useRefund;
        private String bankName;
        private String ifscCode;
        private String bankAccountType;
        private String bankAccountNo;
        private String cashDeposited;
    }
    @Data
    public static class Foreign {
        private String swiftCode;
        private String bankName;
        private String countryCode;
        private String bankAccountNo;
    }

    @Data
    public static class Primary {
        private String bankName;
        private String ifscCode;
        private String cashDeposited;
        private String bankAccountType;
        private String bankAccountNo;
    }
    @Data
    @Builder
    public static class AIS{
        Profile profile;
        List<ItrData> data;
    }
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile{
        private String pan;
        private String aadharNumber;
        private String name;
        private String mobileNumber;
        private String emailAddress;
        private String address;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItrData{
        Object details;

        Object total;

        Object analysis;

        String financialYear;
    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeneralInformation {
        private List<NatOfBusiness> natOfBusiness;
        private String entityPan;
        private String entityName;
        private Address address;
        private String aadharCardNo;
        private String cin;
        private Phone phone;
        private Contact contact;
        private String subStatusOfEntity;
        private String dateOfBirthOrIncorporation;
        private String employerCategory;
        private String domesticCompanyFlag;
        private String statusOfEntity;
        private String dateOfFormOrCommenc;


    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NatOfBusiness {
        private String code;
        private String tradeName1;
        private String title;


    }


    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        private String roadOrStreet;
        private String countryCode;
        private String cityOrTownOrDistrict;
        private String residenceName;
        private String pinCode;
        private String residenceNo;
        private String localityOrArea;
        private String stateCode;


    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Phone {
        private String phoneNo;
        private String stdCode;


    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contact {
        private String mobileNumber;
        private String emailAddressSecondary;
        private String mobileNumberSecondary;
        private String emailAddress;
        private String countryCodeMobileNumberSecondary;
        private String countryCodeMobileNumber;

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopCustomer{

        String customerName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TopSupplier {

        String supplierName;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ItrFilled {
        private String status;
        private String fillingDate;
        private String ackNo;
        private String annualYear;
        private String filingType;
        private String itrForm;
        private List<Activity> activity;
        private String otherFilesDownloadLink;
        private String pan;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Activity {
        private String status;
        private String date;
        private DownloadsStatus downloadsStatus;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public  static  class DownloadsStatus {
        private String downloadLink;
    }

    @Data
    public static class OutstandingDemand {
        private String rectificationRights;
        private String dateOfService;
        private String din;
        private String dateOfDemandRaised;
        private String sectionCode;
        private String assessmentYear;
        private String outstandingDemandAmount;
        private String modeOfService;
        private String uploadedBy;
    }

    @Data
    public static class BadDebtDetails {
        private BadDebt badDebt;
        private ProvisionForBadAndDoubtfulDebts provisionForBadAndDoubtfulDebts;
    }

    @Data
    public static class BadDebt {
        private String totalBadDebt;
        private String othersWherePANNotAvlble;
        private String othersAmtLt1Lakh;
    }

    @Data
    public static class ProvisionForBadAndDoubtfulDebts {
        private String provForBadDoubtDebt;
    }

    public static class AuditInfo {
        private String auditReportSection;
        private String auditReportAct;
        private String auditAccountantFlg;
        private String auditedSection;
        private String liableSec44AAflg;
        private String auditorName;
        private String audFrmPAN;
        private String auditDate;
        private String audFrmName;
        private String auditReportFurnishDate;
        private String auditorMemNo;
        private String liableSec44ABflg;
        private String liableSec92Eflg;
        private String audFrmRegNo;
    }

    @Data
    public static class FormDetails {

        private String financialYear;
        private String description;
        private String assessmentYear;
        private String formVersion;
        private String schemaVersion;
        private String formName;
    }
}
