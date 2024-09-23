package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.LoanStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanApplicationResponseDto extends BasesResponse {

    UUID id;

    Long srCompanyId;

    Long loanVendorId;

    String loanType;

    BigDecimal loanAmountRequested;

    Double interestRate;

    Integer loanDuration;

    LoanStatus status;

    LocalDate startDate;

    LocalDate endDate;
    public static LoanApplicationResponseDto mapLoanApplicationResponse(LoanApplication loanApplication){
        LoanApplicationResponseDto responseDto =new LoanApplicationResponseDto();
        responseDto.setCreatedAt(loanApplication.getAuditData().getCreatedAt());
        responseDto.setCreatedBy(loanApplication.getAuditData().getCreatedBy());
        responseDto.setId(loanApplication.getId());
        responseDto.setStatus(loanApplication.getLoanStatus());
        responseDto.setLoanAmountRequested(loanApplication.getLoanAmountRequested());
        responseDto.setLoanVendorId(loanApplication.getLoanVendorId());
        responseDto.setLoanDuration(loanApplication.getLoanDuration());
       return responseDto;
    }
}
