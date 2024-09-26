package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.LoanApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LoanApplicationStatusRepository extends JpaRepository<LoanApplicationStatus,Long> {

    LoanApplicationStatus findByLoanId(UUID loanId);
}
