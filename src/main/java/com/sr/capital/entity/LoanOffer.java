package com.sr.capital.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_OFFERS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = LOAN_OFFERS)
public class LoanOffer extends UUIDBaseEntity{

   @Column(name = "sr_company_id")
    Long srCompanyId;

   @Column(name = "loan_vendor_id")
   Long loanVendorId;

   @Column(name = "loan_type")
    String loanType;

   @Column(name = "pre_approved")
   Boolean preApproved;

   @Column(name = "pre_approved_loan_amount")
   BigDecimal preApprovedLoanAmount;

   @Column(name = "interest_rate")
    Double interestRate;

    @Column(name = "loan_duration")
    Integer loanDuration;

    String status;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;
}
