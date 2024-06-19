package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.MonthlySalesDetails;
import com.sr.capital.entity.secondary.CapitalDataReport;
import com.sr.capital.repository.secondary.CapitalDataReportRepository;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CapitalDataReportServiceImpl implements CapitalDataReportService {

    final CapitalDataReportRepository capitalDataReportRepository;
    @Override
    public List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId) throws IOException {
        List<CompanySalesDetails> companySalesDetailsList =new ArrayList<>();
        CapitalDataReport dataReport = capitalDataReportRepository.findByCompanyId(Math.toIntExact(srCompanyId));

        if(dataReport!=null){
            CompanySalesDetails companySalesDetails =CompanySalesDetails.builder().srCompanyId(srCompanyId).
            ageInSr(dataReport.getPlatformAgeSignup()).
            orgKyc(dataReport.getOrgType()).
            platformAgeSignupInMonth(dataReport.getPlatformAgeSignupMonth()).
            platformAgeFtrInMonth(dataReport.getPlatformAgeFtsMonth()).
            detailsInfo(MapperUtils.readValue(dataReport.getDetailsInfo(),new TypeReference<Map<String, MonthlySalesDetails>>() {})).build();
            companySalesDetailsList.add(companySalesDetails);
        }
        return companySalesDetailsList;
    }
}
