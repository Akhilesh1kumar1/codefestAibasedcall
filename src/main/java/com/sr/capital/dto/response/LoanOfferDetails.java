package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.primary.LoanOffer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class LoanOfferDetails extends BasesResponse {

    UUID id;

    Long srCompanyId;

    Long loanVendorId;

    String loanType;

    Boolean preApproved;

    BigDecimal preApprovedLoanAmount;

    Double interestRate;

    Integer loanDuration;

    String status;

    LocalDate startDate;

    LocalDate endDate;


    public static LoanOfferDetails mapLoanOffer(LoanOffer loanOffer){
        LoanOfferDetails loanOfferDetails = LoanOfferDetails.builder().id(loanOffer.getId())
                .loanDuration(loanOffer.getLoanDuration()).loanType(loanOffer.getLoanType())
                .endDate(loanOffer.getEndDate()).interestRate(loanOffer.getInterestRate())
                .preApproved(loanOffer.getPreApproved()).loanVendorId(loanOffer.getLoanVendorId()).srCompanyId(loanOffer.getSrCompanyId())
                .preApprovedLoanAmount(loanOffer.getPreApprovedLoanAmount()).startDate(loanOffer.getStartDate()).status(loanOffer.getStatus())
                .build();
        loanOfferDetails.setCreatedAt(loanOffer.getAuditData().getCreatedAt());
        loanOfferDetails.setCreatedBy(loanOffer.getAuditData().getCreatedBy());
        return loanOfferDetails;
    }
}
