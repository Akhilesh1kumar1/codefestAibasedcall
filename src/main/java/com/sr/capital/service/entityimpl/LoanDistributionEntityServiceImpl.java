package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.LoanDisbursed;
import com.sr.capital.repository.primary.LoanDisbursedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanDistributionEntityServiceImpl {

    final LoanDisbursedRepository loanDisbursedRepository;
   public List<LoanDisbursed> getLoanDisbursedDetailsByStatusId(Long loanApplicationStatusId){
        return loanDisbursedRepository.findByLoanApplicationStatusId(loanApplicationStatusId);
    }

    public LoanDisbursed saveLoanDisbursed(LoanDisbursed loanDisbursed){
        return loanDisbursedRepository.save(loanDisbursed);
    }
}
