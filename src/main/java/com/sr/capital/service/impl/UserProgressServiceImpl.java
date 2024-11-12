package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.response.UserProgressResponseDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.Screens;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.repository.primary.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.sr.capital.helpers.enums.Screens.PERSONAL_DETAILS;

@Service
@RequiredArgsConstructor
public class UserProgressServiceImpl {

    final DocDetailsService docDetailsService;
    final UserRepository userRepository;
    final LoanApplicationRepository loanApplicationRepository;


    public UserProgressResponseDto getUserProgress(String tenantId){

        List<LoanApplication> loanApplication = loanApplicationRepository.findBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
        String currentState = Screens.MOBILE_VERIFICATION.name();
        UserProgressResponseDto userProgressResponseDto =UserProgressResponseDto.builder().build();
        if(CollectionUtils.isNotEmpty(loanApplication)){

            currentState = Screens.LOAN_DETAILS.name();

            LoanApplication loanApplication1 = loanApplication.get(loanApplication.size()-1);

            userProgressResponseDto.setLoanId(loanApplication1.getId());
            userProgressResponseDto.setClientLoanId(loanApplication1.getVendorLoanId());
            userProgressResponseDto.setComments(loanApplication1.getComments());
            userProgressResponseDto.setLoanVendorId(loanApplication1.getLoanVendorId());

            switch (loanApplication1.getLoanStatus()){
                case LEAD_INITIATED -> currentState = Screens.LOAN_DETAILS.name();
                case LEAD_VERIFIED ->currentState = PERSONAL_DETAILS.name();
                case LEAD_IN_PROGRESS -> {
                    currentState =loanApplication1.getState();

                    if(loanApplication1.getVendorLoanId()==null && (loanApplication1.getState()!=null && loanApplication1.getState().equalsIgnoreCase(PERSONAL_DETAILS.name()))){
                            currentState = Screens.BUSINESS_DETAILS.name();
                    }

                }
                case LEAD_REJECTED -> currentState = Screens.LEAD_REJECTION.name();
                case LEAD_DOCUMENT_UPLOAD -> currentState =Screens.PENDING_DOCUMENT.name();
                case LEAD_PROCESSING -> currentState = Screens.DOCUMENT_VERIFICATION.name();
                case LOAN_GENERATE -> currentState =Screens.LOAN_SANCTION.name();
                case LOAN_OFFER_DECLINED -> currentState =Screens.LOAN_SANCTION_DECLINED.name();
                case LOAN_VERIFICATION -> currentState =Screens.E_SIGN.name();
                case LOAN_ACCEPTED, LOAN_DISBURSED -> currentState =Screens.LOAN_DISBURSED.name();

            }

            /*if(loanApplication1.getVendorLoanId()!=null) {

                switch (loanApplication1.getLoanStatus()){
                    case LEAD_INITIATED -> currentState = Screens.LOAN_DETAILS.name();
                    case LEAD_VERIFIED ->currentState =Screens.PERSONAL_INFO.name();
                    case LEAD_IN_PROGRESS -> {
                        if(loanApplication1.)
                        currentState =Screens.BUSINESS_DETAILS.name();
                    }
                    case APPROVED -> currentState = Screens.DOCUMENT_VERIFICATION.name();
                }
            }else{
                currentState = Screens.PERSONAL_INFO.name();
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

*/
        }
        userProgressResponseDto.setScreenName(currentState);
        return userProgressResponseDto;
    }
}
