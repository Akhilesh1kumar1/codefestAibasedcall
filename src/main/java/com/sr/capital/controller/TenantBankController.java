package com.sr.capital.controller;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.dto.request.VerifyBankDetails;
import com.sr.capital.dto.response.TenantBankResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.TenantBankService;
import com.sr.capital.service.impl.TenantBankServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.BASE_BANK_CREATED_SUCCESSFULLY;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
@Validated
public class TenantBankController {

    final TenantBankService tenantBankService;

    @PostMapping("/add")
    public GenericResponse<TenantBankResponseDto> addBankDetails(@RequestParam("document")MultipartFile document, @RequestPart BankDetailsRequestDto bankDetailsRequestDto) throws Exception {
        return ResponseBuilderUtil.getResponse(tenantBankService.addBankDetails(bankDetailsRequestDto,document),SUCCESS,
                BASE_BANK_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PostMapping("/verify")
    public GenericResponse<TenantBankResponseDto> addBankDetails( @RequestBody VerifyBankDetails verifyBankDetails) throws Exception {
        return ResponseBuilderUtil.getResponse(tenantBankService.verifyBankDetails(verifyBankDetails),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/{userId}")
    public GenericResponse<List<TenantBankResponseDto>> getBankDetails(@PathVariable(name = "userId")String userId) throws IOException, CustomException {
        return ResponseBuilderUtil.getResponse(tenantBankService.getBankDetails(userId),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }
}
