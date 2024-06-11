package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public GenericResponse<List<LoanOfferDetails>> getLoanOfferDetails(@RequestParam("srCompanyId") Long srCompanyId,@RequestParam(value = "status",required = false) String status,@RequestParam("enabled") Boolean enabled){
        return ResponseBuilderUtil.getResponse(loanOfferService.getLoanOfferDetails(srCompanyId,status,enabled), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
