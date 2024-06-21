package com.sr.capital.repository.secondary;

import com.sr.capital.entity.secondary.CapitalDataReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapitalDataReportRepository extends JpaRepository<CapitalDataReport,Integer> {

    CapitalDataReport findByCompanyId(Integer companyId);

    Page<CapitalDataReport> findAll(Pageable pageable);
}
