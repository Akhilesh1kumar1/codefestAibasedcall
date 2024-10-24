package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.dto.response.DisbursementDetails;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.entity.mongo.SanctionDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.repository.mongo.DisbursmentMongoRepository;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.repository.primary.LoanDisbursedRepository;
import com.sr.capital.service.entityimpl.LoanApplicationStatusEntityServiceImpl;
import com.sr.capital.service.entityimpl.LoanDistributionEntityServiceImpl;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisbursementServiceImpl {


    final LoanApplicationStatusEntityServiceImpl loanApplicationStatusEntityService;
    final LoanApplicationRepository loanApplicationRepository;
    final LoanDistributionEntityServiceImpl loanDistributionService;
    final DisbursmentMongoRepository disbursmentMongoRepository;

    private List<DisbursementDetails> fetchAndSaveDisbursementDetails(LoanMetaDataDto loanMetaDataDto){
        List<DisbursementDetails> disbursementDetails =new ArrayList<>();


        return disbursementDetails;
    }

    private void saveDisbursementDetails(LoanMetaDataDto loanMetaDataDto) {

        if(loanMetaDataDto.getDisbursementDetails()!=null){
            List<Object> disbursementDetailsList =new ArrayList<>();
            DisbursementDetails disbursementDetails =loanMetaDataDto.getDisbursementDetails();
           LoanDisbursed loanDisbursed = LoanDisbursed.builder().loanAmountDisbursed(BigDecimal.valueOf(disbursementDetails.getDisbursementAmount()))
                    .loanApplicationStatusId(loanMetaDataDto.getLoanApplicationStatusId()).durationAtDisbursal(disbursementDetails.getRepaymentPeriod())
                    .interestRateAtDisbursal(disbursementDetails.getInterestRate())
                    .interestAmountAtDisbursal(BigDecimal.valueOf(disbursementDetails.getApprovedAmount()))
                    .vendorDisbursedId(disbursementDetails.getUtrNo()).disbursedDate(disbursementDetails.getDisbursalDate())
                    .build();
            loanDistributionService.saveLoanDisbursed(loanDisbursed);

            disbursementDetailsList.add(disbursementDetails);
            com.sr.capital.entity.mongo.DisbursementDetails disbursementDetails1 = com.sr.capital.entity.mongo.DisbursementDetails.builder()
                    .srCompanyId(loanMetaDataDto.getSrCompanyId()).loanId(loanMetaDataDto.getInternalLoanId())
                    .disbursementDetails(disbursementDetailsList).build();
            disbursmentMongoRepository.save(disbursementDetails1);
        }

    }





}
