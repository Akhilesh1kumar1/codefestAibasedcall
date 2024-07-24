package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.secondary.CompanyWiseReport;
import com.sr.capital.repository.secondary.CompanyWiseReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompanyWiseReportEntityServiceImpl {

    final CompanyWiseReportRepository companyWiseReportRepository;
    public CompanyWiseReport getReportData(Long srCompanyId){
        return companyWiseReportRepository.findById(srCompanyId).orElse(null);
    }
}
