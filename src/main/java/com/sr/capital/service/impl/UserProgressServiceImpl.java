package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.response.UserProgressResponseDto;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.entity.primary.User;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.Screens;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.repository.primary.UserRepository;
import com.sr.capital.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserProgressServiceImpl {

    final DocDetailsService docDetailsService;
    final UserRepository userRepository;
    final LoanApplicationRepository loanApplicationRepository;


    public UserProgressResponseDto getUserProgress(String tenantId){

        List<LoanApplication> loanApplication = loanApplicationRepository.findBySrCompanyIdAndLoanStatus(Long.valueOf(RequestData.getTenantId()), LoanStatus.PENDING);
        String currentState = Screens.LOAN_DETAILS.name();
        UserProgressResponseDto userProgressResponseDto =UserProgressResponseDto.builder().build();
        if(CollectionUtils.isNotEmpty(loanApplication)){

            LoanApplication loanApplication1 = loanApplication.get(0);

            currentState = Screens.PERSONAL_INFO.name();
            userProgressResponseDto.setLoanId(loanApplication1.getId());
            userProgressResponseDto.setClientLoanId(loanApplication1.getVendorLoanId());
            userProgressResponseDto.setComments(loanApplication1.getComments());
            userProgressResponseDto.setLoanVendorId(loanApplication1.getLoanVendorId());

            if(loanApplication1.getLoanStatus().equals(LoanStatus.PRE_APPROVED) ){
                currentState = Screens.BUSINESS_DETAILS.name();

            }
            else if(loanApplication1.getLoanStatus().equals(LoanStatus.PENDING) ){
               currentState = Screens.PENDING_DOCUMENT.name();
            }else{
            User user = userRepository.findTopBySrCompanyId(Long.valueOf(RequestData.getTenantId()));

            if(user!=null) {
                List<KycDocDetails<?>> kycDocDetailsList = docDetailsService.fetchDocDetailsByTenantId(tenantId);
                currentState = Screens.PENDING_DOCUMENT.name();
                Set<String> completedDoc = new HashSet<>();
                if (CollectionUtils.isNotEmpty(kycDocDetailsList)) {

                    kycDocDetailsList.forEach(kycDocDetails -> {
                        completedDoc.add(kycDocDetails.getDocType().name());
                    });

                    if (!completedDoc.contains(DocType.PERSONAL_ADDRESS.name())) {
                        currentState = Screens.PERSONAL_INFO.name();
                    } else if (!completedDoc.contains(DocType.BUSINESS_ADDRESS.name())) {
                        currentState = Screens.BUSINESS_DETAILS.name();
                    }
                }
            }
        }


        }
        userProgressResponseDto.setScreenName(currentState);
        return userProgressResponseDto;
    }
}
