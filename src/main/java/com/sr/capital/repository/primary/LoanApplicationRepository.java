package com.sr.capital.repository.primary;

import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.helpers.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, UUID> {

    List<LoanApplication> findBySrCompanyIdAndIsEnabled(Long srCompanyId,Boolean isEnabled);

    List<LoanApplication> findBySrCompanyId(Long srCompanyId);

    List<LoanApplication> findBySrCompanyIdAndLoanStatus(Long srCompanyId, LoanStatus loanStatus);
}
