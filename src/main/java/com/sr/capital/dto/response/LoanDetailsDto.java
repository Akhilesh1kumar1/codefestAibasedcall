package com.sr.capital.dto.response;

import com.sr.capital.helpers.enums.LoanStatus;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Data
@FieldNameConstants
//@NoArgsConstructor
@Builder

@SqlResultSetMapping(
        name = "LoanDetailsDtoMapping",
        classes = @ConstructorResult(
                targetClass = LoanDetailsDto.class,
                columns = {
                        @ColumnResult(name = "loanId", type = UUID.class),
                        @ColumnResult(name = "srCompanyId", type = Long.class),
                        @ColumnResult(name = "loanVendorId", type = Long.class),
                        @ColumnResult(name = "loanAmountRequested", type = BigDecimal.class),
                        @ColumnResult(name = "loanStatus", type = String.class),
                        @ColumnResult(name = "loanType", type = String.class),
                        @ColumnResult(name = "loanOfferId", type = String.class),
                        @ColumnResult(name = "loanDuration", type = Integer.class),
                        @ColumnResult(name = "loanApplicationStatusId", type = Long.class),
                        @ColumnResult(name = "vendorLoanId", type = String.class),
                        @ColumnResult(name = "loanAmountApproved", type = BigDecimal.class),
                        @ColumnResult(name = "interestRate", type = BigDecimal.class),
                        @ColumnResult(name = "startDate", type = LocalDate.class),
                        @ColumnResult(name = "endDate", type = LocalDate.class),
                        @ColumnResult(name = "disbursedDate", type = LocalDate.class),
                        @ColumnResult(name = "emiAmount", type = BigDecimal.class)
                }
        )
)

public class LoanDetailsDto {
    private UUID loanId;
    private Long srCompanyId;
    private Long loanVendorId;
    private BigDecimal loanAmountRequested;
    private LoanStatus loanStatus;
    private String loanType;
    private Long loanOfferId;
    private Integer loanDuration;
    private Long loanApplicationStatusId;
    private String vendorLoanId;
    private String vendorStatus;
    private BigDecimal loanAmountApproved;
    private Double interestRate;
    private Integer loanApplicationStatusDuration;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime loanCreatedAt;
    private String loanCreatedBy;
    private LocalDateTime loanApplicationStatusCreatedAt;
    private String loanApplicationStatusCreatedBy;
    private LocalDateTime loanApplicationStatusUpdatedAt;
    private BigDecimal totalDisbursedAmount;
    private Date disbursementDate;
    private BigDecimal totalRecoverableAmount;
    private BigDecimal emiAmount;
    private BigDecimal interestAmountAtSanction;

    private Integer durationAtDisbursal;

    private Double interestRateAtDisbursed;

    // Constructor, getters, and setters...
}
