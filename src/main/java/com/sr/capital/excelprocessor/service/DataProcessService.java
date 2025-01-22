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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
            LoanApplication loanApplication = saveIntoLoanDetails(loanDetailsFieldFromExcel);
            LoanApplicationStatus loanApplicationStatus = saveIntoLoanDetailsStatus(loanDetailsFieldFromExcel, loanApplication);
            saveIntoLoanDisbursed(loanDetailsFieldFromExcel, loanApplicationStatus, loanApplication);
            log.info("Data saved into db");
        } catch (Exception e) {
            log.error("Error While saving data:"  + e.getLocalizedMessage());
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
               .emiAmount(BigDecimal.valueOf(loanDetailsFieldFromExcel.getMonthlyEMIAmount() != null ? loanDetailsFieldFromExcel.getMonthlyEMIAmount() : 0))
               .interestRateAtDisbursal(loanDetailsFieldFromExcel.getDisbursementLoanROI() != null ? loanDetailsFieldFromExcel.getDisbursementLoanROI() : 0)
                .interestAmountAtDisbursal(BigDecimal.valueOf(loanDetailsFieldFromExcel.getDisbursementLoanROI()))
//                .vendorDisbursedId(disbursementAccount.getDisbursementId())
                .disbursedDate(loanDetailsFieldFromExcel.getDisbursementDate())
                .build();
        loanDisbursedRepository.save(loanDisbursed);
    }

    private LoanApplication getLoanDetailsModel(LoanDetailsFieldFromExcel loanDetailsFieldFromExcel) {
        BaseCreditPartner creditPartner = null;
        try {
            creditPartner = baseCreditPartnerEntityService.getCreditPartnerByName(loanDetailsFieldFromExcel.getLoanVendorName());
        } catch (Exception e) {
            log.error("Vendor not found with given name " + loanDetailsFieldFromExcel.getLoanVendorName());
        }

        LoanApplication.LoanApplicationBuilder builder = LoanApplication.builder()
                .srCompanyId(Long.valueOf(loanDetailsFieldFromExcel.getCompanyId()))
                .loanAmountRequested(loanDetailsFieldFromExcel.getSanctionAmount() != null ? BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()) : BigDecimal.valueOf(0))
//                .loanOfferId(loanDetailsFieldFromExcel.getLoanOfferId())
                // Todo :: remove static value
                .loanVendorId(creditPartner != null ? creditPartner.getId() : 1)
                .vendorLoanId(String.valueOf(loanDetailsFieldFromExcel.getVendorLoanId()))
                .loanDuration(loanDetailsFieldFromExcel.getSanctionLoanTenure())
                .loanType(loanDetailsFieldFromExcel.getLoanType())
                .state(loanDetailsFieldFromExcel.getState())
                .vendorStatus(loanDetailsFieldFromExcel.getVendorLoanStatus())
                .loanSubmissionTime(convertDateToLocalDateTime(loanDetailsFieldFromExcel.getSanctionDate()))
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
                .startDate(convertDateToLocalDate(loanDetailsFieldFromExcel.getDateOfInitiation()))
                .endDate(convertDateToLocalDate(loanDetailsFieldFromExcel.getLastEMIDate()))
                .loanAmountApproved(BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionAmount()))
                .interestRate(loanDetailsFieldFromExcel.getDisbursementLoanROI())
                .totalRecoverableAmount(loanDetailsFieldFromExcel.getAmountDueTillDate())
                .interestAmountAtSanction(BigDecimal.valueOf(loanDetailsFieldFromExcel.getSanctionLoanROI()))
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

    private LocalDateTime convertDateToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }

        try {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
        } catch (Exception e) {
            log.error("Error while parsing date " + date);
            return null;
        }
    }

    private LocalDate convertDateToLocalDate(Date date) {
        if (date == null) {
            return null;
        }

        try {
            return date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
        } catch (Exception e) {
            log.error("Error while parsing date " + date);
            return null;
        }
    }
}