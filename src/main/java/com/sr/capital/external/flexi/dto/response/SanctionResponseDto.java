package com.sr.capital.external.flexi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SanctionResponseDto {

    private Boolean success;
    private Data data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private PartnerIntegrationProject partnerIntegrationProject;
        private List<String> postSanctionConditionsArray;

    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
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
    public static class DetailsOfCharge {
        private String label;
        private String value;
    }
}
