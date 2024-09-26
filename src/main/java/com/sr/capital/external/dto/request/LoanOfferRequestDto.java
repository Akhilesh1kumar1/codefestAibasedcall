package com.sr.capital.external.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanOfferRequestDto {

    BigDecimal borrowerEligibleLoanAmount;

    Double borrowerEligibleTenure;

    Double borrowerEligibleInterestRate;

    String borrowerEligibleSanctionExpiryDate;

    Double borrowerEligibleProcessingFeePercent;

    Double borrowerEligibleProcessingFee;

    String borrowerEligibleOfferStartDate;

    String programType;

    String loanStatus;

    String loanId;
}
