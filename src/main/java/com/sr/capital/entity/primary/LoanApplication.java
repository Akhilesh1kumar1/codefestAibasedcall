package com.sr.capital.entity.primary;

import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.helpers.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

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

    @Column(name = "loan_offer_id")
    UUID loanOfferId;

    @Column(name = "loan_duration")
    Integer loanDuration;

    @Column(name = "loan_type")
    String loanType;
    public static LoanApplication mapLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto){
        LoanApplication loanApplication =LoanApplication.builder().srCompanyId(loanApplicationRequestDto.getSrCompanyId()).loanAmountRequested(loanApplicationRequestDto.getLoanAmountRequested()).loanOfferId(loanApplicationRequestDto.getLoanOfferId()).loanVendorId(loanApplicationRequestDto.getLoanVendorId()).loanDuration(loanApplicationRequestDto.getLoanDuration()).loanType(loanApplicationRequestDto.getLoanType()).loanStatus(loanApplicationRequestDto.getLoanStatus())
                .build();
        return loanApplication;
    }
}
