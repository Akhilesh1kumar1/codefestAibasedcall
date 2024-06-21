package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.service.ExternalService;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/external")
@RequiredArgsConstructor
@Validated
public class ExternalController {

    final ExternalService externalService;
    @GetMapping("/sales/details/{partnerName}")
    public GenericResponse<List<CompanySalesDetails>> getSalesData(@RequestParam(name = "company_id") Long companyId, @RequestHeader(name = "x-vendor-token") String vendorToken,@RequestHeader(name = "x-vendor-code") String vendorCode,@PathVariable("partnerName") String partnerName) throws Exception {

        return ResponseBuilderUtil.getResponse(externalService.getCompanySalesDetails(companyId,vendorToken,vendorCode,partnerName), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping("/loan/offer")
    public GenericResponse<LoanOfferDetails> saveLoanOffer(@RequestBody LoanOfferDetails loanOfferDetails, @RequestHeader(name = "x-vendor-token") String vendorToken,@RequestHeader(name = "x-vendor-code") String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException {
        return ResponseBuilderUtil.getResponse(externalService.createLoanOffer(loanOfferDetails,vendorToken,vendorCode), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/sales/details/all/{partnerName}")
    public GenericResponse<String> getAllCompanySalesData( @RequestHeader(name = "x-vendor-token") String vendorToken,@RequestHeader(name = "x-vendor-code") String vendorCode,@PathVariable("partnerName") String partnerName) throws Exception {

        return ResponseBuilderUtil.getResponse(externalService.getCompanyWiseSalesDetails(vendorToken,vendorCode,partnerName), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

}
