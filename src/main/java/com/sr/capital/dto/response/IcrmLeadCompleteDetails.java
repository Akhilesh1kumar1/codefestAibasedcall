package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LoanStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class IcrmLeadCompleteDetails {

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String leadId;

    Long srCompanyId;

    String companyName;

    Double cibilScore;

    String panNumber;

    Boolean litigationCheck;

    BigDecimal amountApproved;

    Boolean enachLinking;

    String approvedBy;

    LocalDateTime creditLineApprovalDate;

   UUID internalLoanId;

   String externalLoanId;

   String loanType;

   LocalDateTime dateOfInitiation;

   Long loanVendorId;

   LoanStatus loanStatus;

   BigDecimal sanctionAmount;

   BigDecimal disbursedAmount;

   Integer loanDuration;

}
