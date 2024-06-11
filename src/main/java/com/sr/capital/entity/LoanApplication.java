package com.sr.capital.entity;

import com.sr.capital.helpers.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

import static com.sr.capital.helpers.constants.Constants.EntityNames.LOAN_APPLICATION;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = LOAN_APPLICATION)
public class LoanApplication extends UUIDBaseEntity{

    @Column(name = "sr_company_id")
    Long srCompanyId;

    @Column(name = "loan_vendor_id")
    Long loanVendorId;

    @Column(name = "loan_amount_requested")
    BigDecimal loanAmountRequested;

    @Enumerated(EnumType.STRING)
    @Column(name = "loan_status")
    LoanStatus loanStatus;

}
