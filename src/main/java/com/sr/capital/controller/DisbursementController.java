package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.response.DisbursementDetailsResponseDto;
import com.sr.capital.dto.response.SanctionDto;
import com.sr.capital.service.impl.DisbursementServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/disbursment")
@RequiredArgsConstructor
@Validated
public class DisbursementController {

    final DisbursementServiceImpl disbursementService;
    @GetMapping("")
    public GenericResponse<List<DisbursementDetailsResponseDto>> getSanctionDetails(@RequestParam(name = "loan_application_id") UUID loanApplicationId, @RequestParam(name = "loan_vendor_name") String loanVendorName) throws Exception {

        return ResponseBuilderUtil.getResponse(disbursementService.getDisbursmentDetails(loanApplicationId,loanVendorName), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
