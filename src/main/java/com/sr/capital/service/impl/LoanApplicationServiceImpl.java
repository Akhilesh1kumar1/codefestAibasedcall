package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    final RequestValidationStrategy requestValidationStrategy;
    final LoanApplicationRepository loanApplicationRepository;
    final LoanOfferService loanOfferService;
    @Override
    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) throws Exception {

        loanApplicationRequestDto = requestValidationStrategy.validateRequest(loanApplicationRequestDto, RequestType.LOAN_APPLICATION);
        LoanApplication loanApplication = LoanApplication.mapLoanApplication(loanApplicationRequestDto);
        loanApplication = loanApplicationRepository.save(loanApplication);
        if(loanApplicationRequestDto.getLoanOfferId()!=null)
           loanOfferService.updateLoanOffer(loanApplicationRequestDto.getLoanOfferId(),true);
        return LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication);
    }

    @Override
    public List<LoanApplicationResponseDto> getLoanApplication(UUID loanApplicationId) {

        List<LoanApplicationResponseDto> loanApplicationResponseDtos =new ArrayList<>();

        if(loanApplicationId!=null){
            LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElse(null);
            if(loanApplication!=null){
                loanApplicationResponseDtos.add(LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication));
            }
        }else{
            List<LoanApplication> loanApplications =loanApplicationRepository.findBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
            if(CollectionUtils.isNotEmpty(loanApplications))
                 loanApplications.forEach(loanApplication -> {
                     loanApplicationResponseDtos.add(LoanApplicationResponseDto.mapLoanApplicationResponse(loanApplication));
                 });
        }
        return loanApplicationResponseDtos;
    }
}
