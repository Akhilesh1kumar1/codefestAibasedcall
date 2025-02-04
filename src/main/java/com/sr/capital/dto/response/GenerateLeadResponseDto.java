package com.sr.capital.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GenerateLeadResponseDto {

    String id;

    BigDecimal amount;

    Integer duration;

    LeadStatus status;

    String tier;

    String leadSource;

    String remarks;

    Long loanVendorPartnerId;

    UUID loanApplicationId;

    String utmSource;

    String utmMedium;

    String utmCampaign;

    String utmTerm;

    String utmContent;

    LocalDateTime createdAt;
}
