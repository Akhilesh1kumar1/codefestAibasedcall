package com.sr.capital.service.impl;

import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.dto.response.CompanyKycDetailsResponse;
import com.sr.capital.service.CompanyKycService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@AllArgsConstructor
public class CompanyKycServiceImpl implements CompanyKycService {

    @Override
    public CompanyKycDetailsResponse saveCompanyKycDetais(CompanyKycDetailsRequest companyKycDetailsRequest, MultipartFile multipartFile) {
        return null;
    }
}
