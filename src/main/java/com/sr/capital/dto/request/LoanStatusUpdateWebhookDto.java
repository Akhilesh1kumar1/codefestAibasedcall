package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanStatusUpdateWebhookDto {

    private String clientLoanId;
    private BigDecimal principalAmount;
    private String disbursementType;
    private Double interestRate;
    private Integer tenure;
    private String tenureFrequency;
    private Integer cibilScore;
    private String purpose;
    private String repaymentFrequency;
    private Integer numberOfRepayments;
    private String status;
    private Double advanceEmi;
    private String subCategory;
    private String productId;
    private String lenderLoanId;
    private String lenderApplicationId;
    private String disbursementDate;
    private Double principalOutstanding;
    private String rejectReason;
    private String productCode;
    private String sectorCode;
    private String disbursementCode;
    private String disbursementMode;
    private String agreementSignedErrorMessage;
    private ChargesSplit chargesSplit;
    private List<DisbursementAccount> disbursementAccounts;
    private LimitInformation limitInformation;
    private Insurances insurances;
    private Borrower borrower;
    private BorrowerAdditionalInfo borrowerAdditionalInfo;
    private AdditionalDetails additionalDetails;
    private List<Object> applicants;
    private List<Object> businessApplicants;
    private String errorMessage;
    private Object additionalProp1;

    // Getters and Setters

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class ChargesSplit {
        private ChargeDetail investor;
        private ChargeDetail originator;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class ChargeDetail {
        private BigDecimal principalAmount;
        private Double processingFee;
        private Double stampDuty;
        private Double documentationCharges;
        private Double insuranceCharges;
        private Double differentialInterest;
        private Double otherCharges;
        private Double nachCharges;
        private Double dccCharges;
        private BigDecimal disbursementAmount;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class DisbursementAccount {
        private String accountType;
        private BigDecimal amount;
        private String utrNumber;
        private String disbursedDate;
        private String ifscCode;
        private String bankName;
        private String bankBranchName;
        private Double disbursementAmount;
        private String utrType;
        private String disbursementValueDate;
        private String rpFundAccountId;
        private String rpContactRefId;
        private String dateOfBankAccountOpening;
        private String accountStatus;
        private String fundAccountStatus;
        private String failureReason;
        private String bankBranchAddress;
        private String accountStatusFailureMessage;
        private String originatorUtr;
        private String bankAccountType;
        private Double averageBankDepositMonthly;
        private Double averageBankBalanceMonthly;
        private Double abdToDeclaredRevenue;
        private Double averageExpensesMonthly;
        private String bankBranchMicr;
        private String upiId;
        private String entityType;
        private String bankDetailsVerify;
        private String beneficiaryAccountStatus;
        private String beneficiaryCreatedDate;
        private String beneficiaryId;
        private String accountNo;
        private String disbursementId;
        private String name;
        private Double nameMatchScore;
        private String pennyDropStatus;
        private Double pennyNameMatchScore;
        private String pennyNameMatchStatus;
        private Long id;
        private Double disbursedAmount;
        private Long accountableId;
        private String accountableType;
        private String type;
        private PassThroughData passThroughData;
        private String payoutId;
        private Double principalPercentage;
        private Double processingFeeDeductionPercentage;
        private Double processingFee;
        private Double stampDutyDeductionPercentage;
        private Double stampDuty;
        private Double documentationChargesDeductionPercentage;
        private Double documentationCharges;
        private Double differentialInterestDeductionPercentage;
        private Double differentialInterest;
        private Double insuranceChargesDeductionPercentage;
        private Double insuranceCharges;
        private Double nachChargesDeductionPercentage;
        private Double nachCharges;
        private Double dccChargesDeductionPercentage;
        private Double dccCharges;
        private Double otherChargesDeductionPercentage;
        private Double otherCharges;
        private String accountName;

        // Getters and Setters
        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

        public static class PassThroughData {
            private String type;

            // Getters and Setters
        }
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class LimitInformation {
        private String borrowerLimitAtDisbursal;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class Insurances {
        private List<String> insurances;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class Borrower {
        private String investorCustomerId;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class BorrowerAdditionalInfo {
        private BigDecimal borrowerEligibleLoanAmount;
        private Integer borrowerEligibleTenure;
        private Double borrowerEligibleInterestRate;
        private String borrowerEligibleSanctionExpiryDate;
        private Double borrowerEligibleProcessingFeePercent;
        private Double borrowerEligibleProcessingFee;
        private Double borrowerEligibleOfferStartDate;

        // Getters and Setters
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

    public static class AdditionalDetails {
        private String lenderLoanId;
        private String lenderApplicationId;
        private String agreementId;
        private String allocatedInvestorEntity;
        private String allocationTime;
        private Double bpi;
        private Object escrowValidationStatus;
        private String investorSanctionDealNo;

        //lan_creation_date
        private String lanCreationDate;

        // Getters and Setters
    }
}
