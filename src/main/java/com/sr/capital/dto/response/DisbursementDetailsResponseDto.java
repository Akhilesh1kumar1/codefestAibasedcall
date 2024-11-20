package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@lombok.Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DisbursementDetailsResponseDto {

    private String loanCode;
    private Double approvedAmount;
    private String processingFeeType;
    private Double processingFee;
    private String interestRateType;
    private Double interestRate;
    private String repaymentFrequency;
    private Integer repaymentPeriod;
    private Double emiAmount;
    private Double otherCharges;
    private Double insuranceFees;
    private Double stampDuty;
    private Double disbursementAmount;
    private String firstEmiDate;
    private String differentialInterestRate;
    private String lmsProductCode;
    private String annualizedInterestRate;
    private String disbursalDate;
    private String utrNo;
    private Integer autoApproval;
}
