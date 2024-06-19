package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/external")
@RequiredArgsConstructor
@Validated
public class ExternalController {

    final CapitalDataReportService capitalDataReportService;
    @GetMapping("/sales/details")
    public GenericResponse<List<CompanySalesDetails>> getSalesData(@RequestParam(name = "company_id",required = false) Long companyId, @RequestHeader(name = "x-vendor-token") String vendorToken,@RequestHeader(name = "x-vendor-code") String vendorCode) throws Exception {

        return ResponseBuilderUtil.getResponse(capitalDataReportService.getCompanySalesDetails(companyId), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }


}
