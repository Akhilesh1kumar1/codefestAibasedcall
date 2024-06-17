package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.LoanApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationStatusRepository extends JpaRepository<LoanApplicationStatus,Long> {
}
