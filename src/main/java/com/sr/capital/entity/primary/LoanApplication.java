package com.sr.capital.entity.primary;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.helpers.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@FieldNameConstants
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

    @Column(name = "external_loan_id")
    String vendorLoanId;

    @Column(name = "comments")
    String comments;

    @Column(name = "external_lead_code")
    String externalLeadCode;

    @Column(name = "state")
    String state;

    @Column(name = "vendor_status")
    String vendorStatus;

    @Column(name = "utm_source")
    String utmSource;

    @Column(name = "utm_medium")
    String utmMedium;

    @Column(name = "utm_campaign")
    String utmCampaign;

    @Column(name = "utm_term")
    String utmTerm;

    @Column(name = "utm_content")
    String utmContent;

    @Column(name = "internal_loan_id")
    String internalLoanId;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "loan_submitted_at")
    private LocalDateTime loanSubmissionTime;

    public static LoanApplication mapLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto){
        LoanApplication.LoanApplicationBuilder builder = LoanApplication.builder()
                .srCompanyId(loanApplicationRequestDto.getSrCompanyId())
                .loanAmountRequested(loanApplicationRequestDto.getLoanAmountRequested())
                .loanOfferId(loanApplicationRequestDto.getLoanOfferId())
                .loanVendorId(loanApplicationRequestDto.getLoanVendorId())
                .loanDuration(loanApplicationRequestDto.getLoanDuration())
                .loanType(loanApplicationRequestDto.getLoanType())
                .loanStatus(loanApplicationRequestDto.getLoanStatus());

        if (loanApplicationRequestDto.getUtmSource() != null) {
            builder.utmSource(loanApplicationRequestDto.getUtmSource());
        }
        if (loanApplicationRequestDto.getUtmMedium() != null) {
            builder.utmMedium(loanApplicationRequestDto.getUtmMedium());
        }
        if (loanApplicationRequestDto.getUtmCampaign() != null) {
            builder.utmCampaign(loanApplicationRequestDto.getUtmCampaign());
        }
        if (loanApplicationRequestDto.getUtmTerm() != null) {
            builder.utmTerm(loanApplicationRequestDto.getUtmTerm());
        }
        if (loanApplicationRequestDto.getUtmContent() != null) {
            builder.utmContent(loanApplicationRequestDto.getUtmContent());
        }

        LoanApplication loanApplication = builder.build();
        loanApplication.setIsEnabled(true);
        if(loanApplicationRequestDto.getInternalLoanId()!=null)
           loanApplication.setInternalLoanId(loanApplicationRequestDto.getInternalLoanId());
        return loanApplication;
    }

    public static void mapLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto,LoanApplication loanApplication){
        loanApplication.setLoanAmountRequested(loanApplicationRequestDto.getLoanAmountRequested());
        loanApplication.setLoanDuration(loanApplicationRequestDto.getLoanDuration());
        loanApplication.setLoanStatus(loanApplicationRequestDto.getLoanStatus());
        loanApplication.getAuditData().setUpdatedAt(LocalDateTime.now());
        loanApplication.getAuditData().setUpdatedBy(String.valueOf(RequestData.getUserId()));
        if (loanApplicationRequestDto.getUtmCampaign() != null) {
            loanApplication.setUtmCampaign(loanApplicationRequestDto.getUtmCampaign());
        }
        if (loanApplicationRequestDto.getUtmMedium() != null) {
            loanApplication.setUtmMedium(loanApplicationRequestDto.getUtmMedium());
        }
        if (loanApplicationRequestDto.getUtmContent() != null) {
            loanApplication.setUtmContent(loanApplicationRequestDto.getUtmContent());
        }
        if (loanApplicationRequestDto.getUtmSource() != null) {
            loanApplication.setUtmSource(loanApplicationRequestDto.getUtmSource());
        }
        if (loanApplicationRequestDto.getUtmTerm() != null) {
            loanApplication.setUtmTerm(loanApplicationRequestDto.getUtmTerm());
        }
    }
}
