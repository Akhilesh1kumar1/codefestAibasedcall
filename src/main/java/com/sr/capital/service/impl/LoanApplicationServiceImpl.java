package com.sr.capital.service.impl;

import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanApplicationServiceImpl implements LoanApplicationService {

    final RequestValidationStrategy requestValidationStrategy;
    final LoanApplicationRepository loanApplicationRepository;
    @Override
    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplicationRequestDto) throws Exception {
        loanApplicationRequestDto = requestValidationStrategy.validateRequest(loanApplicationRequestDto, RequestType.LOAN_APPLICATION);
        LoanApplication loanApplication = MapperUtils.convertValue(loanApplicationRequestDto,LoanApplication.class);
        loanApplication = loanApplicationRepository.save(loanApplication);
        return MapperUtils.convertValue(loanApplication,LoanApplicationResponseDto.class);
    }
}
