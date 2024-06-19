package com.sr.capital.repository.secondary;

import com.sr.capital.entity.secondary.CapitalDataReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapitalDataReportRepository extends JpaRepository<CapitalDataReport,Integer> {

    CapitalDataReport findByCompanyId(Integer companyId);
}
