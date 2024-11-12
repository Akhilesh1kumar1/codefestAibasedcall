package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IcrmLoanRequestDto {
    Long srCompanyId;

    Long loanApplicationStatusId;

    String externalLoanIdOfVendor;

    String emailId;

    String phoneNumber;

    String status;

    String createdAt;

    String createdAtEnd;

    String dateOfSanction;

    String dateOfSanctionEnd;

    String dateOfDisbursal;

    String dateOfDisbursalEnd;

    List<Long> loanVendorIds;

    List<String> loanStatuses;

    Integer pageNumber=1;

    Integer pageSize =20;

    Integer noOfRecord ;

    UUID loanId;

    Boolean isTesting=false;

    String userEmail;
}
