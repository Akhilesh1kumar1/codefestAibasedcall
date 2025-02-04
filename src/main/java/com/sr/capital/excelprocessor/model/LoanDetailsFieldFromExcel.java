package com.sr.capital.excelprocessor.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Setter
@Getter
public class LoanDetailsFieldFromExcel {
    private Integer rowId;
    private String shipRocketApplicationId;
    private Date dateOfInitiation;
    private String loanType;
    private Integer companyId;
    private String companyName;
    private String companyTier;
    private Integer vendorLoanId;
    private String loanVendorName;
    private String shipRocketLoanStatus;
    private String vendorLoanStatus;
    private Date sanctionDate;
    private Date disbursementDate;
    private Double sanctionAmount;
    private Integer sanctionLoanTenure;
    private Double sanctionLoanROI;
    private Double disbursementAmount;
    private Integer disbursementLoanTenure;
    private Double disbursementLoanROI;
    private Double totalRecoverableAmount;
    private Double monthlyEMIAmount;
    private Date nextEMIDate;
    private Date lastEMIDate;
    private Double totalRepaymentAmountReceived;
    private String aumCollection;
    private BigDecimal principalOutstanding;
    private BigDecimal interestOutstanding;
    private BigDecimal totalPrincipalPaid;
    private BigDecimal totalInterestPaid;
    private BigDecimal totalLpiPaid;
    private BigDecimal totalBouncePaid;
    private BigDecimal totalPaidAmount;
    private BigDecimal principalOverdue;
    private BigDecimal interestOverdue;
    private BigDecimal lpiOverdue;
    private BigDecimal bounceOverdue;
    private BigDecimal totalOverdueAmount;
    private Integer dpd;
    private String dpdBucket;
    private BigDecimal amountCollectedLast30Days;
    private BigDecimal amountCollectedLast60Days;
    private String repaymentMtd;
    private BigDecimal amountDueCurrMonth;
    private BigDecimal excessPayment;
    private String loanStatus;
    private BigDecimal waiverAmount;
    private BigDecimal amountDueTillDate;
    private String programCode;
    private String city;
    private String state;
    private String pinCode;
    private Double processingFeeRate;
    private String ever30Plus;
    private String ever60Plus;
    private String ever90Plus;
    private String ever180Plus;
    private Date lastEmiPaidDate;
    private String lastPaymentMode;
    private BigDecimal lastPaymentAmount;
    private Integer futureEmiPendingCount;
    private String panNumber;
    private String gstNumber;
    private String udhyam;
    private String udhyamNumber;
    private String currentStatus;
    private String message;

}