package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.UpdateSanctionOfferDto;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.external.flexi.dto.response.KfsResponseDto;
import com.sr.capital.service.impl.SanctionServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/sanction")
@RequiredArgsConstructor
@Validated
public class SanctionController {

    final SanctionServiceImpl sanctionService;
    @GetMapping("")
    public GenericResponse<SanctionDto> getSanctionDetails(@RequestParam(name = "loan_id") UUID loanApplicationId,@RequestParam(name = "loan_vendor_name") String loanVendorName) throws Exception {

        return ResponseBuilderUtil.getResponse(sanctionService.getSanctionDetails(loanApplicationId,loanVendorName), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping("/offer")
    public GenericResponse<Boolean> acceptOffer(@RequestBody UpdateSanctionOfferDto acceptSanctionOffer) throws Exception {

        return ResponseBuilderUtil.getResponse(sanctionService.updateOffer(acceptSanctionOffer), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/kfs")
    public GenericResponse<KfsResponseDto> getKfsDetails(@RequestParam(name = "loan_id") UUID loanApplicationId,@RequestParam(name = "loan_vendor_name") String loanVendorName) throws Exception {

        return ResponseBuilderUtil.getResponse(sanctionService.getKfsDetails(loanApplicationId,loanVendorName), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

}
