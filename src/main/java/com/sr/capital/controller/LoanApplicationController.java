package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.LoanApplicationRequestDto;
import com.sr.capital.dto.response.LoanApplicationResponseDto;
import com.sr.capital.service.LoanApplicationService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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
}
