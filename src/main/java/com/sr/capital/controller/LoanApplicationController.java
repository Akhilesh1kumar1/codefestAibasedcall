package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.CreateLoanAtVendorRequest;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.request.PendingDocumentRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.dto.response.PendingDocumentResponseDto;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/loan")
@RequiredArgsConstructor
@Validated
public class LoanApplicationController {

   final LoanApplicationService loanApplicationService;
    @PostMapping("/submit")
    public GenericResponse<LoanApplicationResponseDto> submitLoanApplication(@RequestBody LoanApplicationRequestDto loanApplicationRequestDto) throws Exception {

        return ResponseBuilderUtil.getResponse(loanApplicationService.submitLoanApplication(loanApplicationRequestDto), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/details")
    public GenericResponse<List<LoanApplicationResponseDto>> getLoanApplications(@RequestParam(name = "loan_application_id",required = false)UUID loanApplicationId) throws Exception {

        return ResponseBuilderUtil.getResponse(loanApplicationService.getLoanApplication(loanApplicationId), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping("/pending/document")
    public GenericResponse<PendingDocumentResponseDto> getLoanApplications(@Valid @RequestBody PendingDocumentRequestDto pendingDocumentRequestDto) throws Exception {

        return ResponseBuilderUtil.getResponse(loanApplicationService.fetchPendingDocuments(pendingDocumentRequestDto), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @PostMapping("/vendor")
    public GenericResponse<LoanApplicationResponseDto> submitLoanToVendor(@RequestBody CreateLoanAtVendorRequest loanApplicationRequestDto) throws Exception {

        return ResponseBuilderUtil.getResponse(loanApplicationService.createLoanAtVendor(loanApplicationRequestDto), SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
