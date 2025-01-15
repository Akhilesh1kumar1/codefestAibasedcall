package com.sr.capital.excelprocessor.service;

import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.excelprocessor.model.LoanDetailsFieldFromExcel;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.repository.primary.LoanApplicationStatusRepository;
import com.sr.capital.repository.primary.LoanDisbursedRepository;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import com.sr.capital.util.CoreUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.sr.capital.excelprocessor.util.LoanDetailsConstants.FAILED;

@Service
@Slf4j
@RequiredArgsConstructor
public class DataProcessService {

    private final LoanApplicationRepository loanApplicationRepository;
    private final LoanDisbursedRepository loanDisbursedRepository;
    private final LoanApplicationStatusRepository loanApplicationStatusRepository;
    private final BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;

    @Transactional(rollbackFor = Exception.class)
    public void saveDataIntoDb(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel) {
        try {
//            BaseCreditPartner creditPartner = baseCreditPartnerEntityService.getCreditPartnerByName(loanDetailsFieldFromExcel.getLoanVendorName());
            LoanApplication loanApplication = saveIntoLoanDetails(loanDetailsFieldFromExcel);
            LoanApplicationStatus loanApplicationStatus = saveIntoLoanDetailsStatus(loanDetailsFieldFromExcel, loanApplication);
            saveIntoLoanDisbursed(loanDetailsFieldFromExcel, loanApplicationStatus, loanApplication);
            log.info("Data saved into db");
        } catch (Exception e) {
            log.error("Error While saving data:  + e.getLocalizedMessage()");
            loanDetailsFieldFromExcel.setMessage("Error While saving data: " + e.getLocalizedMessage());
            loanDetailsFieldFromExcel.setCurrentStatus(FAILED);
            throw e;
        }
    }

    private void saveIntoLoanDisbursed(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel, LoanApplicationStatus loanApplicationStatusId, LoanApplication loanApplication) {
        LoanDisbursed loanDisbursed = LoanDisbursed.builder()
                .loanAmountDisbursed(BigDecimal.valueOf(loanDetailsFieldFromExcel.getDisbursementAmount()))
                .loanApplicationStatusId(loanApplicationStatusId.getId())
                .durationAtDisbursal(loanDetailsFieldFromExcel.getSanctionLoanTenure())
                .loanId(loanApplication.getId())
                .disbursedDate(loanDetailsFieldFromExcel.getDisbursementDate())
                .loanAmountDisbursed(BigDecimal.valueOf(loanDetailsFieldFromExcel.getDisbursementAmount()))
//                .interestRateAtDisbursal(loanDetailsFieldFromExcel.getInterestOutstanding() != null ? Double.valueOf(loanDetailsFieldFromExcel.getInterestOutstanding()) : 0)
//                .interestAmountAtDisbursal(loanDetailsFieldFromExcel.getInterestAmountAtSanction())
//                .vendorDisbursedId(disbursementAccount.getDisbursementId())
                .disbursedDate(CoreUtil.convertTOdate(String.valueOf(loanDetailsFieldFromExcel.getDisbursementDate()), "yyyy-MM-dd"))
                .build();
        loanDisbursedRepository.save(loanDisbursed);
    }

    private LoanApplication getLoanDetailsModel(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel) {
        LoanApplication.LoanApplicationBuilder builder = LoanApplication.builder()
                .srCompanyId(Long.valueOf(loanDetailsFieldFromExcel.getCompanyId()))
                .loanAmountRequested(loanDetailsFieldFromExcel.getSanctionAmount() != null ? BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()) : BigDecimal.valueOf(0))
//                .loanOfferId(loanDetailsFieldFromExcel.getLoanOfferId())
                .loanVendorId(Long.valueOf(loanDetailsFieldFromExcel.getVendorLoanId()))
                .loanDuration(loanDetailsFieldFromExcel.getSanctionLoanTenure())
                .loanType(loanDetailsFieldFromExcel.getLoanType())
                .state(loanDetailsFieldFromExcel.getState())
                .vendorStatus(loanDetailsFieldFromExcel.getVendorLoanStatus())
                .loanStatus(LoanStatus.LOAN_DISBURSED);

        builder.utmSource("ICRM");
        return builder.build();
    }

    private LoanApplicationStatus saveIntoLoanDetailsStatus(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel, LoanApplication loanApplication) {
        LoanApplicationStatus loanApplicationStatus = LoanApplicationStatus.builder()
                .loanId(loanApplication.getId())
                .loanAmountApproved(BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()))
                .vendorLoanId(loanDetailsFieldFromExcel.getVendorLoanId() != null ? String.valueOf(loanDetailsFieldFromExcel.getVendorLoanId()) : "")
                .vendorStatus(loanDetailsFieldFromExcel.getVendorLoanStatus())
//                .comment(loanDetailsFieldFromExcel.getRejectReason())
                .loanAmountApproved(BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()))
//                .interestRate(loanDetailsFieldFromExcel.getInterestRate())
//                .interestAmountAtSanction(BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()))
                .loanDuration(loanDetailsFieldFromExcel.getSanctionLoanTenure()).
                totalDisbursedAmount(BigDecimal.valueOf(loanDetailsFieldFromExcel.getDisbursementAmount()))
                .totalAmountRecovered(loanDetailsFieldFromExcel.getSanctionAmount() != null ? BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()) : BigDecimal.valueOf(0))
                .build();

        return loanApplicationStatusRepository.save(loanApplicationStatus);
    }

    private LoanApplication saveIntoLoanDetails(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel) {
        LoanApplication loanDetailsModel = getLoanDetailsModel(loanDetailsFieldFromExcel);
        return loanApplicationRepository.save(loanDetailsModel);
    }
}