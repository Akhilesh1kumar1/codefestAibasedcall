package com.sr.capital.service;

import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.entity.LoanApplication;

public interface LoanApplicationService {

    public LoanApplicationResponseDto submitLoanApplication(LoanApplicationRequestDto loanApplication) throws Exception;
}
