package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanOfferDetails extends BasesResponse {

    UUID id;

    Long srCompanyId;

    Long loanVendorId;

    String loanType;

    Boolean preApproved;

    BigDecimal preApprovedLoanAmount;

    Double interestRate;

    Integer loanDuration;

    String status;

    LocalDate startDate;

    LocalDate endDate;
}
