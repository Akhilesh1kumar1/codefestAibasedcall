package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.response.IcrmLeadRsponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.IcrmLeadService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.CREDIT_PARTNER_CREATED_SUCCESSFULLY;

@RestController
@RequestMapping("/api/v1/icrm")
@RequiredArgsConstructor
@Validated
public class IcrmController {

    final IcrmLeadService icrmLeadService;

    @PostMapping()
    public GenericResponse<IcrmLeadRsponseDto> getLeadDetails(@RequestBody IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {

        return ResponseBuilderUtil.getResponse(icrmLeadService.getLeadDetails(icrmLeadRequestDto),SUCCESS,
                CREDIT_PARTNER_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }
}
