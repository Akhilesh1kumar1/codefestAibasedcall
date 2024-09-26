package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.primary.LoanApplicationStatus;
import com.sr.capital.repository.primary.LoanApplicationStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanApplicationStatusEntityServiceImpl {

    final LoanApplicationStatusRepository loanApplicationStatusRepository;

   public LoanApplicationStatus getLoanApplicationStatusById(Long id){
        return loanApplicationStatusRepository.findById(id).orElse(null);
    }

    public LoanApplicationStatus saveLoanApplicationStatus(LoanApplicationStatus loanApplicationStatus){
       return loanApplicationStatusRepository.save(loanApplicationStatus);
    }

    public LoanApplicationStatus getLoanApplicationStatusByLoanId(UUID id){
        return loanApplicationStatusRepository.findByLoanId(id);
    }
}
