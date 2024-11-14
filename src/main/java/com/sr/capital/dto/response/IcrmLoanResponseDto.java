package com.sr.capital.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class IcrmLoanResponseDto {

    Long totalLeads;

    Long totalLeadsToday;

    Long approved;

    Long rejected;

    Long goneCold;

    List<IcrmLoanCompleteDetails> completeDetails;

    PaginationInfo paginationInfo;
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    @Data
   public static class PaginationInfo{
        Integer pageSize;

        Long noOfRecords;

        Integer pageNumber;

        Integer noOfPages;
    }
}
