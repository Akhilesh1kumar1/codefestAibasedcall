package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.omunify.restutil.exceptions.InvalidResourceException;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.entity.mongo.LoanMetaData;
import com.sr.capital.entity.mongo.kyc.child.Checkpoints;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.Screens;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.entityimpl.*;
import com.sr.capital.service.strategy.StatusMapperServiceStrategy;
import com.sr.capital.util.CoreUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class LoanStatusUpdateHandlerServiceImpl {

    final LoanApplicationRepository loanApplicationRepository;
    final LoanDistributionEntityServiceImpl loanDistributionService;
    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final WebhookDetailsEntityServiceImpl webhookDetailsEntityService;
    final StatusMapperServiceStrategy statusMapperServiceStrategy;
    final LoanMetaDataEntityServiceImpl loanMetaDataEntityService;

    public void handleStatusUpdate(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto,String loanVendorName){

        if(loanStatusUpdateWebhookDto!=null && loanStatusUpdateWebhookDto.getLoanCode()!=null){
           updateStatus(loanStatusUpdateWebhookDto,loanVendorName);

        }

    }

    private void updateStatus(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto, String loanVendorName) {

        LoanApplication loanApplication =null;

        if(loanStatusUpdateWebhookDto.getClientLoanId()!=null)
           loanApplication = loanApplicationRepository.findById(UUID.fromString(loanStatusUpdateWebhookDto.getClientLoanId())).orElse(null);

        if(loanApplication==null && loanStatusUpdateWebhookDto.getLoanCode()!=null){
             loanApplication =  loanApplicationRepository.findByVendorLoanId(loanStatusUpdateWebhookDto.getLoanCode());
         }


        if(loanApplication!=null){

            loanStatusUpdateWebhookDto.setInternalCurrentStatus(loanApplication.getLoanStatus().name());

            loanStatusUpdateWebhookDto = statusMapperServiceStrategy.getPartnerService(loanVendorName).mapStatus(loanStatusUpdateWebhookDto);


            loanStatusUpdateWebhookDto.setStatus(loanStatusUpdateWebhookDto.getInternalStatus());
            loanApplication.setState(loanStatusUpdateWebhookDto.getInternalState());
            loanApplication.setComments(loanStatusUpdateWebhookDto.getS3());
            saveLoanMetaData(loanApplication,loanStatusUpdateWebhookDto);
            loanApplication.setComments(loanStatusUpdateWebhookDto.getS3());
            loanApplication.setLoanStatus(LoanStatus.valueOf(loanStatusUpdateWebhookDto.getInternalStatus()));
            loanApplication.setVendorStatus(loanStatusUpdateWebhookDto.getStatus());
            switch (LoanStatus.valueOf(loanStatusUpdateWebhookDto.getStatus())){

                /*case LEAD_PROCESSING:
                    loanApplication.setLoanStatus(LoanStatus.valueOf(loanStatusUpdateWebhookDto.getInternalStatus()));
                    //updateLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);
                    break;

                case DISBURSED:
                    loanApplication.setLoanStatus(LoanStatus.valueOf(loanStatusUpdateWebhookDto.getInternalStatus()));
                   // saveDisbursementDetails(loanStatusUpdateWebhookDto,loanApplication);
                    break;
                case REJECTED:
                    loanApplication.setLoanStatus(LoanStatus.valueOf(loanStatusUpdateWebhookDto.getInternalStatus()));
                   // updateLoanApplicationStatus(loanStatusUpdateWebhookDto,loanApplication);*/

            }
            loanApplication.setState(loanStatusUpdateWebhookDto.getInternalState());


        }else{
            log.info("invalid webhook {} ",loanStatusUpdateWebhookDto);
            throw new InvalidResourceException("Invalid client_loan_id");
        }

        loanApplicationRepository.save(loanApplication);
      /*  WebhookDetails webhookDetails =WebhookDetails.builder().clientLoanId(loanApplication.getId()).srCompanyId(loanApplication.getSrCompanyId()).loanType("loan").loanWebhookData(loanStatusUpdateWebhookDto).build();
        webhookDetailsEntityService.saveWebhookDetails(webhookDetails);*/

    }

    private void saveLoanMetaData(LoanApplication loanApplication, LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto) {

        LoanMetaData loanMetaData = loanMetaDataEntityService.getLoanMetaDataDetails(loanApplication.getId());
        if(loanMetaData==null){
            TypeReference<List<Checkpoints>> tref =new TypeReference<List<Checkpoints>>() {
            };
            List<Checkpoints> checkpoints = MapperUtils.convertValue(loanStatusUpdateWebhookDto.getCheckpoints(),tref);

            loanMetaData =LoanMetaData.builder().loanId(loanApplication.getId()).checkPoints(checkpoints).externalStatus1(loanMetaData.getExternalStatus1())
                    .externalStatus2(loanMetaData.getExternalStatus2()).externalStatus3(loanMetaData.getExternalStatus3()).leadCode(loanStatusUpdateWebhookDto.getLeadCode())
                    .externalApplicationStatus(loanStatusUpdateWebhookDto.getApplicationStatus()).build();
        }else{

            TypeReference<List<Checkpoints>> tref =new TypeReference<List<Checkpoints>>() {
            };
            List<Checkpoints> checkpoints = MapperUtils.convertValue(loanStatusUpdateWebhookDto.getCheckpoints(),tref);

            loanMetaData.setCheckPoints(checkpoints);
            loanMetaData.setExternalStatus1(loanStatusUpdateWebhookDto.getS1());
            loanMetaData.setExternalStatus2(loanStatusUpdateWebhookDto.getS2());
            loanMetaData.setExternalStatus3(loanStatusUpdateWebhookDto.getS3());
            loanMetaData.setExternalApplicationStatus(loanStatusUpdateWebhookDto.getApplicationStatus());
        }
        loanMetaDataEntityService.saveLoanMetaData(loanMetaData);

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
                             .vendorDisbursedId(disbursementAccount.getDisbursementId()).disbursedDate(CoreUtil.convertTOdate(disbursementAccount.getDisbursedDate(), "yyyy-MM-dd"))
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

    public LoanApplication updateLoanState(UUID id , DocType type) {
        LoanStatus loanStatus = LoanStatus.LEAD_IN_PROGRESS;
        String state = "PERSONAL_DETAILS";

        switch (type){
            case PERSONAL_ADDRESS -> state = Screens.PERSONAL_DETAILS.name();
            case BUSINESS_ADDRESS -> state =Screens.BUSINESS_DETAILS.name();
            default -> {
                state = "DOCUMENT_UPLOAD";
                loanStatus = LoanStatus.LEAD_DOCUMENT_UPLOAD;
            }
        }

        LoanApplication loanApplication =loanApplicationRepository.findById(id).orElse(null);
        if(loanApplication!=null && (loanApplication.getLoanStatus()!=loanStatus || loanApplication.getState()!=state)){
            loanApplication.setLoanStatus(loanStatus);
            loanApplication.setState(state);
            loanApplicationRepository.save(loanApplication);
        }

        return loanApplication;
    }
}
