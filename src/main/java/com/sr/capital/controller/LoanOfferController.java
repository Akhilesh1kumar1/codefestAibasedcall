package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.response.LoanOfferDetails;
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
@RequestMapping("/api/loan/offer")
@RequiredArgsConstructor
@Validated
public class LoanOfferController {

    final LoanOfferService loanOfferService;

    @GetMapping
    public GenericResponse<List<LoanOfferDetails>> getLoanOfferDetails(@RequestParam(value = "srCompanyId",required = false) Long srCompanyId,@RequestParam(value = "status",required = false) String status,@RequestParam("enabled") Boolean enabled){
        return ResponseBuilderUtil.getResponse(loanOfferService.getLoanOfferDetails(Long.valueOf(RequestData.getTenantId()),status,enabled), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping("/save")
    public GenericResponse<LoanOfferDetails> saveLoanOffer(@RequestBody LoanOfferDetails loanOfferDetails){
        return ResponseBuilderUtil.getResponse(loanOfferService.saveLoanOffer(loanOfferDetails), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
