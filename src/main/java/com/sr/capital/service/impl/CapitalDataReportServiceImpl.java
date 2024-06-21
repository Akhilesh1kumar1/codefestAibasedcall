package com.sr.capital.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.MonthlySalesDetails;
import com.sr.capital.entity.secondary.CapitalDataReport;
import com.sr.capital.repository.secondary.CapitalDataReportRepository;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
         CompanySalesDetails companySalesDetails = buildCompanySalesDetails(dataReport);
          companySalesDetailsList.add(companySalesDetails);
        }
        return companySalesDetailsList;
    }

    private CompanySalesDetails buildCompanySalesDetails(CapitalDataReport dataReport) throws IOException {
        CompanySalesDetails companySalesDetails =CompanySalesDetails.builder().srCompanyId(dataReport.getCompanyId().longValue()).
                ageInSr(dataReport.getPlatformAgeSignup()).
                orgKyc(dataReport.getOrgType()).
                platformAgeSignupInMonth(dataReport.getPlatformAgeSignupMonth()).
                platformAgeFtrInMonth(dataReport.getPlatformAgeFtsMonth()).
                detailsInfo(MapperUtils.readValue(dataReport.getDetailsInfo(),new TypeReference<Map<String, MonthlySalesDetails>>() {})).build();
        return companySalesDetails;

    }

    @Override
    public List<CompanySalesDetails> getAllCompanySalesDetails() {
        Integer pageNumber =0;
        Integer pageSize =50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "companyId"));

        List<CapitalDataReport> capitalDataReportList =new ArrayList<>();
        List<CompanySalesDetails> companySalesDetailsList = new ArrayList<>();
        Page<CapitalDataReport> dataReportList = getAllData(pageable);

        while (dataReportList.hasNext()){
            capitalDataReportList.addAll(dataReportList.getContent());
            pageable =PageRequest.of(++pageNumber, pageSize, Sort.by(Sort.Direction.ASC, "companyId"));
            dataReportList = getAllData(pageable);
        }

        if(!capitalDataReportList.isEmpty()){
            capitalDataReportList.forEach(capitalDataReport -> {
                try {
                    companySalesDetailsList.add(buildCompanySalesDetails(capitalDataReport));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return companySalesDetailsList;
    }

    private Page<CapitalDataReport> getAllData(Pageable pageable){
        return capitalDataReportRepository.findAll(pageable);
    }
}
