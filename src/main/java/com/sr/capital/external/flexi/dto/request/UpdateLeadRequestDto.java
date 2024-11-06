package com.sr.capital.external.flexi.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateLeadRequestDto {


    private String leadCode;
    private String firstName;
    private String lastName;
    private String mobileNo;
    private String email;
    private LoanDetails loanApplication;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanDetails {
        private String loanCode;
        private BigDecimal amount;
        private String partnerRefNo;
        private Boolean termsConditionAcceptance;
        @JsonProperty("loanApplicant")
        private LoanApplicant loanApplicant;
        @JsonProperty("loanBusiness")
        private LoanBusiness loanBusiness;
        @JsonProperty("loanFinance")
        private LoanFinance loanFinance;
        @JsonProperty("loanBusinessPartners")
        private List<LoanBusinessPartner> loanBusinessPartners;
        @JsonProperty("loanPersonalReferences")
        private List<LoanPersonalReference> loanPersonalReferences;

        // Getters and setters for each field
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanApplicant {
        private String loanCode;
        private String dob;
        private String gender;
        private String panNo;
        private String pincode;
        @JsonProperty("address_line_1")
        private String addressLine1;
        @JsonProperty("address_line_2")
        private String addressLine2;
        private String ownershipStatus;
        private String whatsappNo;
        private String alternateMobileNo;
        private Integer isCurrentAccountAvailable;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanBusiness {
        private String loanCode;
        private String legalStatus;
        private String businessName;
        @JsonProperty("address_line_1")
        private String addressLine1;
        @JsonProperty("address_line_2")
        private String addressLine2;
        private String pincode;
        private Integer partnerCount;
        private String businessPanNo;
        private String gstNo;
        private String dateOfIncorporation;
        private String ownershipStatus;
        private Integer hasGstRegistration;

        // Getters and setters for each field
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanFinance {
        private String loanCode;
        private BigDecimal monthlyTotalSales;
        private BigDecimal monthlyOnlineSales;
        private BigDecimal monthlyOfflineSales;
        private BigDecimal turnoverLastYear;
        private BigDecimal profitLastYear;
        private BigDecimal turnoverProjected;
        private BigDecimal profitProjected;
        private BigDecimal monthlyEmi;
        private BigDecimal profitMargin;
        private BigDecimal monthlyPosSales;
        private String posSalesPartner;

        // Getters and setters for each field
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanBusinessPartner {
        private String loanCode;
        private String name;
        private String panNo;
        private String mobileNo;
        private String gender;
        private String dob;
        private Double holding;
        private String address;
        private String city;
        private String state;
        private String pincode;
        private String ownershipStatus;
        private String interimBusinessPartnerIdentifier;

        // Getters and setters for each field
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class LoanPersonalReference {
        private String loanCode;
        private String name;
        private String mobileNo;
        private String profession;
        private Integer yearsOfKnowability;
        private String email;
        private String relationship;
        private String interimPersonalReferenceIdentifier;

        // Getters and setters for each field
    }

}
