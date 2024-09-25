package com.sr.capital.external.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanOfferRequestDto {

    BigDecimal borrowerEligibleLoanAmount;

    BigDecimal borrowerEligibleTenure;
}
