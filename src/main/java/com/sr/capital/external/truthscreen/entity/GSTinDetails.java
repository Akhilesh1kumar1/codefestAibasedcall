package com.sr.capital.external.truthscreen.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GSTinDetails {

    private String administrativeOffice;
    private String annualAggregateTurnover;
    private String constitutionOfBusiness;
    private String dateOfCancellation;
    private String dateOfRegistration;
    private String gstinUinStatus;
    private String gstinUin;
    private String grossTotalIncome;
    private String legalNameOfBusiness;
    private String natureOfBusinessActivities;
    private String natureOfCoreBusinessActivity;
    private String otherOffice;
    private String percentageOfTaxPaymentInCash;
    private String taxpayerType;
    private String tradeName;
    private String whetherAadhaarAuthenticated;
    private String whetherEKYCVerified;
    private String fieldVisitConducted;
}
