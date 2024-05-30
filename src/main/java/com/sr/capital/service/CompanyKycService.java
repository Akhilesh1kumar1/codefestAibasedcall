package com.sr.capital.service;

import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.dto.response.CompanyKycDetailsResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CompanyKycService {

    CompanyKycDetailsResponse saveCompanyKycDetais(CompanyKycDetailsRequest companyKycDetailsRequest, MultipartFile multipartFile);
}
