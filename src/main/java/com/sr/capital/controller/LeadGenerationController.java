package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.CreateBaseCreditPartnerDto;
import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.service.LeadGenerationService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/lead")
@Validated
@RequiredArgsConstructor
public class LeadGenerationController {

    final LeadGenerationService leadGenerationService;
    @PostMapping("/save")
    public GenericResponse<GenerateLeadResponseDto> createLead(@RequestBody GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        return ResponseBuilderUtil.getResponse(leadGenerationService.saveLead(generateLeadRequestDto),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/details")
    public GenericResponse<List<GenerateLeadResponseDto>> getAllLeads() throws CustomException {
        return ResponseBuilderUtil.getResponse(leadGenerationService.getAllLeads(Long.valueOf(RequestData.getTenantId())),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }


}
