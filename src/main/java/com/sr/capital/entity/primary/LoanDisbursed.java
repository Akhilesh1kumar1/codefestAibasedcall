package com.sr.capital.entity.primary;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_DISBURSED;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = LOAN_DISBURSED)
@FieldNameConstants
public class LoanDisbursed extends LongBaseEntity{

    @Column(name = "loan_application_status_id")
    Long loanApplicationStatusId;

    @Column(name = "loan_id")
    UUID loanId;

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


    @Column(name = "emi_amount")
    BigDecimal emiAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "disbursed_date")
    Date disbursedDate;
}
