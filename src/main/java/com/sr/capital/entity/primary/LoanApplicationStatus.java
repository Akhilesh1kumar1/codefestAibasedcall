package com.sr.capital.entity.primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_APPLICATION;
import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_APPLICATION_STATUS;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = LOAN_APPLICATION_STATUS)
@FieldNameConstants
public class LoanApplicationStatus extends LongBaseEntity{

    @Column(name = "loan_id")
    UUID loanId;

    @Column(name = "vendor_loan_id")
    String vendorLoanId;

    @Column(name = "loan_amount_approved")
    BigDecimal loanAmountApproved;

    @Column(name = "interest_rate")
    Double interestRate;

    @Column(name = "loan_duration")
    Integer loanDuration;

    @Column(name = "interest_amount_at_sanction")
    @Builder.Default
    BigDecimal interestAmountAtSanction=BigDecimal.ZERO;

    @Column(name = "start_date")
    LocalDate startDate;

    @Column(name = "end_date")
    LocalDate endDate;

    @Column(name = "comment")
    String comment;

    @Column(name = "total_disbursed_amount")
    @Builder.Default
    BigDecimal totalDisbursedAmount=BigDecimal.ZERO;

    @Column(name = "vendor_status")
    String vendorStatus;

    @Column(name = "sanction_code")
    String sanctionCode;

    @Column(name = "total_amount_recovered")
    BigDecimal totalAmountRecovered;

    @Column(name = "total_recoverable_amount")
    BigDecimal totalRecoverableAmount;
    
}
