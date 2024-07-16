package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LoanStatus;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
//@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoanApplicationStatusDto {

    UUID loanId;

    Long srCompanyId;

    Long loanVendorId;

    BigDecimal loanAmountRequested;

    LoanStatus loanStatus;

    //UUID loanOfferId;

    Integer loanDuration;

    Long loanApplicationStatusId;

    String vendorLoanId;

    BigDecimal loanAmountApproved;

    Double interestRate;

    Integer loanDurationByVendor;

   /* @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    LocalDate endDate;*/


    private LocalDateTime loanCreatedAt;

    private String loanCreatedBy;

    LocalDateTime loanApprovedAt;

    String loanApprovedBy;

    LocalDateTime loanStatusUpdatedAt;

    /*public LoanApplicationStatusDto(UUID loanId, Long srCompanyId, Long loanVendorId, BigDecimal loanAmountRequested, LoanStatus loanStatus, UUID loanOfferId, Integer loanDuration, Long loanApplicationStatusId, String vendorLoanId, BigDecimal loanAmountApproved, Double interestRate, Integer loanDurationByVendor, LocalDate startDate, LocalDate endDate, LocalDateTime loanCreatedAt, String loanCreatedBy, LocalDateTime loanApprovedAt, String loanApprovedBy, LocalDateTime loanStatusUpdatedAt) {
        this.loanId = loanId;
        this.srCompanyId = srCompanyId;
        this.loanVendorId = loanVendorId;
        this.loanAmountRequested = loanAmountRequested;
        this.loanStatus = loanStatus;
        this.loanOfferId = loanOfferId;
        this.loanDuration = loanDuration;
        this.loanApplicationStatusId = loanApplicationStatusId;
        this.vendorLoanId = vendorLoanId;
        this.loanAmountApproved = loanAmountApproved;
        this.interestRate = interestRate;
        this.loanDurationByVendor = loanDurationByVendor;
        this.startDate = startDate;
        this.endDate = endDate;
        this.loanCreatedAt = loanCreatedAt;
        this.loanCreatedBy = loanCreatedBy;
        this.loanApprovedAt = loanApprovedAt;
        this.loanApprovedBy = loanApprovedBy;
        this.loanStatusUpdatedAt = loanStatusUpdatedAt;
    }*/

    /*public LoanApplicationStatusDto(UUID loanId, Long srCompanyId, Long loanVendorId, BigDecimal loanAmountRequested){
        this.loanId = loanId;
        this.srCompanyId = srCompanyId;
        this.loanVendorId = loanVendorId;
        this.loanAmountRequested = loanAmountRequested;
    }*/


}
