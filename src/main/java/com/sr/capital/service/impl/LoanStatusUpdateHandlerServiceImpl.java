package com.sr.capital.service.impl;

import com.omunify.restutil.exceptions.InvalidResourceException;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.entity.mongo.WebhookDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.service.entityimpl.LoanDistributionEntityServiceImpl;
import com.sr.capital.service.entityimpl.WebhookDetailsEntityServiceImpl;
import com.sr.capital.service.entityimpl.WebhookHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

import static com.sr.capital.helpers.enums.LoanStatus.APPROVED;
import static com.sr.capital.helpers.enums.LoanStatus.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoanStatusUpdateHandlerServiceImpl {

    final LoanApplicationRepository loanApplicationRepository;
    final LoanDistributionEntityServiceImpl loanDistributionService;
    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final WebhookDetailsEntityServiceImpl webhookDetailsEntityService;

    public void handleStatusUpdate(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto,String loanVendorName){

        if(loanStatusUpdateWebhookDto!=null && loanStatusUpdateWebhookDto.getLoanCode()!=null){

            if(loanStatusUpdateWebhookDto.getApplicationStatus()!=null ){

                updateStatus(loanStatusUpdateWebhookDto,loanVendorName);
            }
        }

    }

    private void updateStatus(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto, String loanVendorName) {

        LoanApplication loanApplication = loanApplicationRepository.findById(UUID.fromString(loanStatusUpdateWebhookDto.getClientLoanId())).orElse(null);
         if(loanApplication==null && loanStatusUpdateWebhookDto.getLoanCode()!=null){
             loanApplication =  loanApplicationRepository.findByVendorLoanId(loanStatusUpdateWebhookDto.getLoanCode());
         }

        if(loanApplication!=null){
            loanStatusUpdateWebhookDto.setStatus(loanStatusUpdateWebhookDto.getApplicationStatus().toUpperCase());
            switch (LoanStatus.valueOf(loanStatusUpdateWebhookDto.getApplicationStatus())){
                case APPROVED:
                    loanApplication.setLoanStatus(APPROVED);
                    //updateLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);
                    break;

                case DISBURSED:
                    loanApplication.setLoanStatus(LoanStatus.DISBURSED);
                   // saveDisbursementDetails(loanStatusUpdateWebhookDto,loanApplication);
                    break;
                case REJECTED:
                    loanApplication.setLoanStatus(REJECTED);
                   // updateLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);
            }


        }else{
            log.info("invalid webhook {} ",loanStatusUpdateWebhookDto);
            throw new InvalidResourceException("Invalid client_loan_id");
        }

        loanApplicationRepository.save(loanApplication);
      /*  WebhookDetails webhookDetails =WebhookDetails.builder().clientLoanId(loanApplication.getId()).srCompanyId(loanApplication.getSrCompanyId()).loanType("loan").loanWebhookData(loanStatusUpdateWebhookDto).build();
        webhookDetailsEntityService.saveWebhookDetails(webhookDetails);*/

    }

    private void updateLoanApplicationStatus(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto, LoanApplication loanApplication) {
        buildLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);

    }

    private void saveDisbursementDetails(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto,LoanApplication loanApplication) {

      LoanApplicationStatus loanApplicationStatus =  buildLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);

        loanStatusUpdateWebhookDto.getDisbursementAccounts().forEach(disbursementAccount -> {
            if (disbursementAccount.getDisbursementId() != null && disbursementAccount.getDisbursedAmount()!=null) {
                LoanDisbursed loanDisbursed = loanDistributionService.getLoanDisbursedDetailsByStatusIdAndVendorDisbursedId(loanApplicationStatus.getId(), disbursementAccount.getDisbursementId());
                 if(loanDisbursed==null) {
                     loanDisbursed = LoanDisbursed.builder().loanAmountDisbursed(BigDecimal.valueOf(disbursementAccount.getDisbursedAmount()))
                             .loanApplicationStatusId(loanApplicationStatus.getId()).durationAtDisbursal(loanApplicationStatus.getLoanDuration())
                             .interestRateAtDisbursal(loanStatusUpdateWebhookDto.getInterestRate())
                             .interestAmountAtDisbursal(loanApplicationStatus.getInterestAmountAtSanction())
                             .vendorDisbursedId(disbursementAccount.getDisbursementId()).disbursedDate(disbursementAccount.getDisbursedDate())
                             .build();
                     loanDistributionService.saveLoanDisbursed(loanDisbursed);
                 }
            }
        });

    }

    private LoanApplicationStatus buildLoanApplicationStatus(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto, LoanApplication loanApplication) {

        LoanApplicationStatus loanApplicationStatus = loanApplicationStatusEntityService.getLoanApplicationStatusByLoanId(loanApplication.getId());

        if(loanApplicationStatus==null) {
            BigDecimal totalDisbursementAmount = BigDecimal.ZERO;
            loanStatusUpdateWebhookDto.getDisbursementAccounts().forEach(disbursementAccount -> {
                        if (disbursementAccount.getDisbursementId() != null && disbursementAccount.getDisbursedAmount()!=null) {
                            totalDisbursementAmount.add(BigDecimal.valueOf(disbursementAccount.getDisbursedAmount()));
                        }
                    });
            loanApplicationStatus = LoanApplicationStatus.builder().loanId(loanApplication.getId())
                    .vendorLoanId(loanApplication.getId().toString()).vendorStatus(loanStatusUpdateWebhookDto.getStatus())
                    .comment(loanStatusUpdateWebhookDto.getRejectReason())
                    .loanAmountApproved(loanStatusUpdateWebhookDto.getPrincipalAmount())
                    .interestRate(loanStatusUpdateWebhookDto.getInterestRate())
                    .interestAmountAtSanction(loanStatusUpdateWebhookDto.getPrincipalAmount().multiply(BigDecimal.valueOf(loanStatusUpdateWebhookDto.getInterestRate())).divide(BigDecimal.valueOf(100)))
                    .loanDuration(loanStatusUpdateWebhookDto.getTenure()).
                    totalDisbursedAmount(totalDisbursementAmount)
                    .build();
            loanApplicationStatusEntityService.saveLoanApplicationStatus(loanApplicationStatus);
        }
        return loanApplicationStatus;
    }
}
