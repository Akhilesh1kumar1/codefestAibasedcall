package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanStatusUpdateHandlerServiceImpl {

    final LoanApplicationRepository loanApplicationRepository;

    public void handleStatusUpdate(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto,String loanVendorName){

        if(loanStatusUpdateWebhookDto!=null && loanStatusUpdateWebhookDto.getAdditionalDetails()!=null){

            if(loanStatusUpdateWebhookDto.getStatus()!=null && loanStatusUpdateWebhookDto.getStatus().equalsIgnoreCase(LoanStatus.APPROVED.name())){

                LoanApplication loanApplication = loanApplicationRepository.findById(UUID.fromString(loanStatusUpdateWebhookDto.getClientLoanId())).orElse(null);

                if(loanApplication!=null && (loanApplication.getLoanStatus().equals(LoanStatus.PENDING) || loanApplication.getLoanStatus().equals(LoanStatus.PRE_APPROVED))){
                    loanApplication.setLoanStatus(LoanStatus.APPROVED);
                    loanApplicationRepository.save(loanApplication);
                }
            }
        }

    }
}
