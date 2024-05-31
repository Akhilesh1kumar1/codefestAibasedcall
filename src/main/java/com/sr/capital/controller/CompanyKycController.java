package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.dto.request.UpdateCompanyKycDetails;
import com.sr.capital.dto.response.CompanyKycDetailsResponse;
import com.sr.capital.service.CompanyKycService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.COMPANY_KYC_SAVED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
@Validated
public class CompanyKycController {

    final CompanyKycService companyKycService;


    @PostMapping("/kyc/save")
    public GenericResponse<CompanyKycDetailsResponse> saveKycDetails(@RequestPart CompanyKycDetailsRequest companyKycDetailsRequest , @RequestParam("document") MultipartFile document) throws Exception {
        return ResponseBuilderUtil.getResponse(companyKycService.saveCompanyKycDetais(companyKycDetailsRequest,document),SUCCESS,
                COMPANY_KYC_SAVED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PutMapping("/kyc/update")
    public GenericResponse<CompanyKycDetailsResponse> updateKycDetails(@RequestPart UpdateCompanyKycDetails updateCompanyKycDetails , @RequestParam("documents") List<MultipartFile> documents) throws Exception {
        return ResponseBuilderUtil.getResponse(companyKycService.updateCompanyKycDetails(updateCompanyKycDetails,documents),SUCCESS,
                COMPANY_KYC_SAVED_SUCCESSFULLY, HttpStatus.SC_OK);
    }
}
