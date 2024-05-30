package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.CreateBaseAccountTypeDto;
import com.sr.capital.dto.response.BaseAccountTypeResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.entityimpl.AccountTypeEntityServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import org.apache.http.HttpStatus;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.BASE_ACCOUNT_TYPE_CREATED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/account/type")
@RequiredArgsConstructor
@Validated
public class BaseAccountTypeController {

    final AccountTypeEntityServiceImpl accountTypeEntityService;
    @PostMapping("/create")
    public GenericResponse<Boolean> addBaseAccountType(@RequestBody CreateBaseAccountTypeDto createBaseAccountTypeDto) throws CustomException, IOException {
        return ResponseBuilderUtil.getResponse(accountTypeEntityService.createBaseAccountType(createBaseAccountTypeDto),SUCCESS,
                BASE_ACCOUNT_TYPE_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @GetMapping()
    public GenericResponse<List<BaseAccountTypeResponseDto>> getAllAccountTypes() {
        return ResponseBuilderUtil.getResponse(accountTypeEntityService.getAllAccountTypes(),SUCCESS,
                "", HttpStatus.SC_OK);
    }

    @GetMapping("/sync/all")
    public GenericResponse<Boolean> syncAllBaseAccountType() throws CustomException, IOException {
        return ResponseBuilderUtil.getResponse(accountTypeEntityService.syncAllAccountTypesToCache(),SUCCESS,
                BASE_ACCOUNT_TYPE_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }
}
