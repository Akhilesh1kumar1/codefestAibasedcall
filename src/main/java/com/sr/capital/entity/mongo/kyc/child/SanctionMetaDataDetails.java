package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.dto.response.SanctionDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class SanctionMetaDataDetails {

    PartnerIntegrationProject partnerIntegrationProject;
    private List<String> postSanctionConditionsArray;


    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PartnerIntegrationProject {
        private List<String> tnc;
        private String product;
        private String sanction_code;
        private String totalGst;
        private String emiAmount;
        private List<FeeCharge> feeCharges;
        private String loanAmount;
        private String totalInterest;
        private String totalRepayable;
        private String disbursalAmount;
        private String processingFeeOg;
        private String totalFeeCharges;
        private List<DetailsOfCharge> detailsOfCharges;
        private String processingFeeGst;
        private String documentChargesOg;
        private String interestFrequency;
        private String documentChargesGst;
        private String loanRepaymentPeriod;
        private String annualRateOfInterest;
        private String monthlyRateOfInterest;
        private String loanRepaymentFrequency;
        private String postSanctionConditions;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeeCharge {
        private String key;
        private String raw;
        private int index;
        private String label;
        private String value;
        private String sub_label;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DetailsOfCharge {
        private String label;
        private String value;
    }
}
