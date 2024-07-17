package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.LoanDisbursed;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanDisbursedRepository extends JpaRepository<LoanDisbursed,Long> {

    List<LoanDisbursed> findByLoanApplicationStatusId(Long loanApplicationStatusId);
}
