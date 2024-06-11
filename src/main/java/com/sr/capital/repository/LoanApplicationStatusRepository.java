package com.sr.capital.repository;

import com.sr.capital.entity.LoanApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanApplicationStatusRepository extends JpaRepository<LoanApplicationStatus,Long> {
}
