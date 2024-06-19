package com.sr.capital.service;

import com.sr.capital.dto.response.CompanySalesDetails;

import java.io.IOException;
import java.util.List;

public interface CapitalDataReportService {


    List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId) throws IOException;
}
