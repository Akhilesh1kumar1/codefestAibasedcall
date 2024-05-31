package com.sr.capital.service;

import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.dto.request.UpdateCompanyKycDetails;
import com.sr.capital.dto.response.CompanyKycDetailsResponse;
import com.sr.capital.exception.custom.CustomException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CompanyKycService {

    CompanyKycDetailsResponse saveCompanyKycDetais(CompanyKycDetailsRequest companyKycDetailsRequest, MultipartFile multipartFile) throws Exception;

    CompanyKycDetailsResponse updateCompanyKycDetails(UpdateCompanyKycDetails companyKycDetailsRequest , List<MultipartFile> multipartFileList) throws CustomException, IOException;
}
