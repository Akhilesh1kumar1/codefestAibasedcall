package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.truthscreen.entity.FilingData;
import com.sr.capital.external.truthscreen.entity.PlaceOfBusiness;
import com.sr.capital.external.truthscreen.entity.Service;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GSTinExtractionResponseData {

    @JsonProperty("AdministrativeOffice")
    private String administrativeOffice;
    @JsonProperty("AnnualAggregateTurnover")
    private String annualAggregateTurnover;
    @JsonProperty("ConstitutionOfBusiness")
    private String constitutionOfBusiness;
    @JsonProperty("Date of Cancellation")
    private String dateOfCancellation;
    @JsonProperty("Date of registration")
    private String dateOfRegistration;
    @JsonProperty("GSTIN / UIN Status")
    private String gstinUinStatus;
    @JsonProperty("GSTIN/ UIN")
    private String gstinUin;
    @JsonProperty("GrossTotalIncome")
    private String grossTotalIncome;
    @JsonProperty("Legal Name of Business")
    private String legalNameOfBusiness;
    @JsonProperty("NatureOfBusinessActivities")
    private String natureOfBusinessActivities;
    @JsonProperty("NatureOfCoreBusinessActivity")
    private String natureOfCoreBusinessActivity;
    @JsonProperty("OtherOffice")
    private String otherOffice;
    @JsonProperty("PercentageOfTaxPaymentInCash")
    private String percentageOfTaxPaymentInCash;
    @JsonProperty("Taxpayer Type")
    private String taxpayerType;
    @JsonProperty("Trade Name")
    private String tradeName;
    @JsonProperty("WhetherAadhaarAuthenticated")
    private String whetherAadhaarAuthenticated;
    @JsonProperty("WhetherE-KYCVerified")
    private String whetherEKYCVerified;
    @JsonProperty("field_visit_conducted")
    private String fieldVisitConducted;
    @JsonProperty("filingData")
    private Map<String, List<FilingData>> filingData;
    @JsonProperty("goods_n_service")
    private Map<String, List<Service>> goodsAndService;
    @JsonProperty("placeOfBusinessData")
    private List<PlaceOfBusiness> placeOfBusinessData;
    @JsonProperty("proprietor_name")
    private List<String> proprietorName;
}
