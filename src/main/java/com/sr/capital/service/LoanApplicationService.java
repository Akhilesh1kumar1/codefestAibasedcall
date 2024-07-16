package com.sr.capital.service;

import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.dto.response.LoanApplicationStatusDto;
import com.sr.capital.entity.primary.LoanApplication;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationService {

    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplication) throws Exception;

    public List<LoanApplicationResponseDto> getLoanApplication(UUID loanApplicationId);

    public List<Object[]> getLoanApplicationStatusByCompanyId(Long srCompanyId);


}
