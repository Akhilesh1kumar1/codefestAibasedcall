package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.LoanStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class IcrmLoanCompleteDetails {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;

    String leadId;

    Long srCompanyId;

    String companyName;

    Double cibilScore;

    String panNumber;

    Boolean litigationCheck;

    BigDecimal amountApproved;

    Boolean enachLinking;

    String approvedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime creditLineApprovalDate;

   UUID internalLoanId;

   String externalLoanId;

   String loanType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dateOfInitiation;

   Long loanVendorId;

   LoanStatus loanStatus;

   String externalLoanStatus;

   BigDecimal sanctionAmount;

   BigDecimal disbursedAmount;

   Integer loanDurationAtSanction;

    Long loanApplicationStatusId;

   List<DisburseDetails> disburseDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime documentCompletedAt;

    Double interestRate;

    BigDecimal interestAmountAtSanction;


    String vendorStatus;

    String zipLink;

    String loanVendorName;

    String tier;

    Double interestRateAtDisbursal;

    BigDecimal interestAmountAtDisbursal;

    Integer disbursementTenure;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    String disbursedDate;

    BigDecimal totalRecoverableAmount;

    BigDecimal monthlyEmi;

    Date nextEmiDate;

    Date lastEmiDate;

    BigDecimal totalRecoveredAmount;

    BigDecimal LoanAmountRequested;

    String userName;

    String emailId;

    String mobileNumber;
    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DisburseDetails{

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt;

        BigDecimal disbursedAmount;

        Double interestRate;

        BigDecimal interestAmountAtDisbursal;

        Integer tenure;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        Date disbursedDate;
    }

}
