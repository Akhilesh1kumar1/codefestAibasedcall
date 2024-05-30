package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.CreateBaseBankDto;
import com.sr.capital.dto.response.BaseBankResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.entityimpl.BankEntityServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.apache.http.HttpStatus;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.*;

@RestController
@RequestMapping("/api/base/bank")
@RequiredArgsConstructor
@Validated
public class BaseBankController {

    final BankEntityServiceImpl bankEntityService;

    @PostMapping("/create")
    public GenericResponse<Boolean> addBaseBank(@RequestBody CreateBaseBankDto createBaseBankDto) throws CustomException {
        return ResponseBuilderUtil.getResponse(bankEntityService.addBank(createBaseBankDto),SUCCESS,
                BASE_BANK_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @GetMapping()
    public GenericResponse<List<BaseBankResponseDto>> getAllBaseBanks() {
        return ResponseBuilderUtil.getResponse(bankEntityService.getAllBanks(),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @GetMapping("/sync")
    public GenericResponse<Boolean> syncAllBaseBankToCache() {
        return ResponseBuilderUtil.getResponse(bankEntityService.syncAllBaseBankToCache(),SUCCESS,
                "", HttpStatus.SC_OK);
    }
}
