package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import com.sr.capital.dto.request.UpdateBaseCreditPartnerDto;
import com.sr.capital.dto.response.BaseCreditPartnerResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.CREDIT_PARTNER_CREATED_SUCCESSFULLY;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.CREDIT_PARTNER_UPDATED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/credit/partner")
@RequiredArgsConstructor
@Validated
public class BaseCreditPartnerController {

    final BaseCreditPartnerEntityServiceImpl baseCreditPartnerService;


    @PostMapping("/create")
    public GenericResponse<Boolean> createCreditPartner(@RequestBody CreateBaseCreditPartnerDto createBaseCreditPartnerDto) throws CustomException {
        return ResponseBuilderUtil.getResponse(baseCreditPartnerService.createBaseCreditPartner(createBaseCreditPartnerDto),SUCCESS,
                CREDIT_PARTNER_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PutMapping("/update")
    public GenericResponse<Boolean> updateCreditPartner(@RequestBody UpdateBaseCreditPartnerDto updateBaseCreditPartnerDto) throws CustomException {
        return ResponseBuilderUtil.getResponse(baseCreditPartnerService.updateBaseCreditPartner(updateBaseCreditPartnerDto),SUCCESS,
                CREDIT_PARTNER_UPDATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @GetMapping()
    public GenericResponse<List<BaseCreditPartnerResponseDto>> getAllCreditPartners() {
        return ResponseBuilderUtil.getResponse(baseCreditPartnerService.getAllCreditPartner(),SUCCESS,
                "", HttpStatus.SC_OK);
    }
    @GetMapping("/sync/all")
    public GenericResponse<Boolean> syncAllBaseCreditPartnerToCache() {
        return ResponseBuilderUtil.getResponse(baseCreditPartnerService.syncAllBaseCreditPartnerToCache(),SUCCESS,
                "", HttpStatus.SC_OK);
    }
}
