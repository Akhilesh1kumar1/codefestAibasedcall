package com.sr.capital.dto.response;

import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class LeadDetailsResponseDto {

    List<LeadDetails> leadList;
    private int totalPages;
    private long totalElements;
    private int pageNumber;
    private int pageSize;

    @Data
    @Builder
   public static class LeadDetails{

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

    }
}
