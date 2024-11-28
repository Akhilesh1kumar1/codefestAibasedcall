package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GenerateLeadRequestDto {

    String leadId;

    BigDecimal amount;

    Integer duration;

    LeadStatus status;

    String tier;

    String leadSource;

    String remarks;

    Long loanVendorPartnerId;

    UUID loanApplicationId;

    String userName;

    Long srCompanyId;

    String mobileNumber;

    String utmSource;

    String utmMedium;

    String utmCampaign;

    String utmTerm;

    String utmContent;

}
