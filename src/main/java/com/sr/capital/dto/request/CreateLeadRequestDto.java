package com.sr.capital.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateLeadRequestDto {

    String customerCategory;
    String clientCustomerId;
    String firstName;
    String lastName;
    String dateOfBirth;
    String gender;
    String primaryBorrowerType;
    String currentAddress;
    String currentCity;
    String currentState;
    String currentPincode;
    String mobileNumber;
    String panNumber;
    String productId;
    String applicationId;
    String clientLoanId;
    String category;
    String subCategory;
    BigDecimal principalAmount;
    String email;
    String permanentAddress;
    String permanentCity;
    String permanentState;
    String permanentPincode;
    String nameOfBureau;
    Long bureauScore;
    List<String> bureauReportLink;
    Business business;
    List<DisbursementAccount> disbursementAccounts;

    @Builder.Default
    String disbursementType = "Single Disbursement";

    @Builder.Default
    Double interestRate =14.5;

    Integer tenure;

    @Builder.Default
    String tenureFrequency = "monthly";

    @Builder.Default
    String repaymentFrequency= "Once";

    Integer numberOfRepayments ;


    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Business {
        String nameOfBusiness;
        String typeOfConstitution;
        String industryType;
        String businessType;
        Long businessVintage;
        String sectorType;
        String businessRegisteredOfficeAddress;
        String businessRegisteredOfficeState;
        Long businessRegisteredOfficePincode;
        List<Long> businessPhoneNumber;
        String businessPanNumber;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class DisbursementAccount {
        String bankName;
        String accountName;
        String bankBranchName;
        String ifscCode;
        String entityType;
        String accountNo;
        String bankAccountType;
    }
}
