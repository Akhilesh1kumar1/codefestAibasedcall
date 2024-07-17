package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LoanStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IcrmLeadRequestDto {

    Long srCompanyId;

    Long loanApplicationStatusId;

    String externalLoanIdOfVendor;

    String emailId;

    String phoneNumber;

    String status;

    String createdAt;

    String dateOfSanction;

    String dateOfDisbursal;

    List<Long> loanVendorIds;

    List<LoanStatus> loanStatuses;

    Integer pageNumber=1;

    Integer pageSize =20;

   Integer noOfRecord ;

   UUID loanId;

}
