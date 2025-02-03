package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.corpveda.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PartnerDetailsSubMetaData {

    @JsonProperty("sr_company_id")
    private String srCompanyId;
    @JsonProperty("message")
    private String message;
    @JsonProperty("type")
    private String type;
    @JsonProperty("contact_details")
    private ContactDetails contactDetails;
    @JsonProperty("basic_details")
    private BasicDetails basicDetails;
    @JsonProperty("industry_segments")
    private IndustrySegments industrySegments;
    @JsonProperty("company_name_changes")
    private List<CompanyNameChange> companyNameChanges;
    @JsonProperty("company_address_changes")
    private List<AddressChange> addressChanges;
    @JsonProperty("current_director")
    private List<Director> companyDirector;
    @JsonProperty("other_directorship")
    private List<OtherDirectorship> otherDirectorship;
    @JsonProperty("company_charges")
    private List<CompanyCharge> companyCharge;
    @JsonProperty("share_holdings")
    private List<ShareHoldings> shareHoldings;
    @JsonProperty("advance_financial_data")
    private AdvanceFinancialData advanceFinancialData;
    @JsonProperty("company_auditors")
    private CompanyAuditors companyAuditors;
    @JsonProperty("company_secretary")
    private CompanySecretary companySecretary;
    @JsonProperty("company_holdings")
    private List<CompanyHolding> companyHoldings;
    @JsonProperty("company_subsidiaries")
    private List<CompanySubsidiary> companySubsidiaries;
    @JsonProperty("common_directors")
    private List<CommonDirector> commonDirectors;
    @JsonProperty("msme_supplier_payment_details")
    private List<MsmeSupplierPaymentDetail> msmeSupplierPaymentDetails;
    @JsonProperty("securities_allotment")
    private List<SecuritiesAllotment> securitiesAllocations;
    @JsonProperty("ts_gstin_details")
    private TsGstinDetails tsGstinDetails;
    @JsonProperty("annexures")
    private List<Annexure> annexures;
    @JsonProperty("code")
    private int code;
    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("report_status")
    private String reportStatus;
    @JsonProperty("download_link")
    private String downloadLink;
}
