package com.sr.capital.entity.primary;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_DISBURSED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = LOAN_DISBURSED)
public class LoanDisbursed extends LongBaseEntity{

    @Column(name = "loan_application_status_id")
    Long loanApplicationStatusId;

    @Column(name = "loan_amount_disbursed")
    BigDecimal loanAmountDisbursed;

    @Column(name = "interest_rate_at_disbursal")
    Double interestRateAtDisbursal;

    @Column(name = "interest_amount_at_disbursal")
    BigDecimal interestAmountAtDisbursal;

    @Column(name = "duration_at_disbursal")
    Integer durationAtDisbursal;

    @Column(name = "vendor_disbursed_id")
    String vendorDisbursedId;
}
