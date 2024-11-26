package com.sr.capital.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LeadDetailsResponseDto {

    List<LeadDetails> leadList;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class LeadDetails {

        String leadId;

        Long srCompanyId;

        BigDecimal amount;

        Integer duration;

        LeadStatus status;

        UUID loanApplicationId;

        String tier;

        String leadSource;

        String remarks;

        Long loanVendorPartnerId;

        String companyName;

        String brandName;

        LocalDateTime createdAt;

        LocalDateTime updatedAt;

        String userName;

        String mobileNumber;

        String utmSource;

        String utmMedium;

        String utmCampaign;

        String utmTerm;

        String utmContent;

    }
}
