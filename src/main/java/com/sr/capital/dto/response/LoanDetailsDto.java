package com.sr.capital.dto.response;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class LoanDetailsDto {
    private Long loanId;
    private Long srCompanyId;
    private Long loanVendorId;
    private BigDecimal loanAmountRequested;
    private String loanStatus;
    private String loanType;
    private Long loanOfferId;
    private Integer loanDuration;
    private Long loanApplicationStatusId;
    private String vendorLoanId;
    private BigDecimal loanAmountApproved;
    private BigDecimal interestRate;
    private Integer loanApplicationStatusDuration;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime loanCreatedAt;
    private String loanCreatedBy;
    private LocalDateTime loanApplicationStatusCreatedAt;
    private String loanApplicationStatusCreatedBy;
    private LocalDateTime loanApplicationStatusUpdatedAt;
    private BigDecimal totalDisbursedAmount;
    private LocalDate disbursementDate;

    // Constructor, getters, and setters...
}
