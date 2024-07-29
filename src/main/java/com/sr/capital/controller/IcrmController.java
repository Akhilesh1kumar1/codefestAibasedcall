package com.sr.capital.controller;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.response.*;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.LeadStatus;
import com.sr.capital.service.IcrmLeadService;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.CREDIT_PARTNER_CREATED_SUCCESSFULLY;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
@RequestMapping("/api/v1/icrm")
@RequiredArgsConstructor
@Validated
public class IcrmController {

    final IcrmLeadService icrmLeadService;

    @PostMapping("/loan/details")
    public GenericResponse<IcrmLoanResponseDto> getLoanDetails(@RequestBody IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {

        return ResponseBuilderUtil.getResponse(icrmLeadService.getLoanDetails(icrmLeadRequestDto),SUCCESS,
                CREDIT_PARTNER_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PostMapping("/loan/details/download")
    public GenericResponse<Boolean> downloadLoanReport(@RequestBody IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {
             icrmLeadService.downloadLoanReport(icrmLeadRequestDto);
        return ResponseBuilderUtil.getResponse(true,SUCCESS,
                CREDIT_PARTNER_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PostMapping("/loan/complete/details")
    public GenericResponse<IcrmLoanResponseDto> getCompleteDetails(@RequestBody IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException {

        return ResponseBuilderUtil.getResponse(icrmLeadService.getCompleteLoanDetails(icrmLeadRequestDto),SUCCESS,
                CREDIT_PARTNER_CREATED_SUCCESSFULLY, HttpStatus.SC_OK);
    }

    @PutMapping("/lead/update")
    public GenericResponse<GenerateLeadResponseDto> updateLead(@RequestBody GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        return ResponseBuilderUtil.getResponse(icrmLeadService.updateLead(generateLeadRequestDto),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/lead/details")
    public GenericResponse<LeadDetailsResponseDto> getLeadDetails(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate, @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size, @RequestParam(value = "type",required = false) String type) throws CustomException {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseBuilderUtil.getResponse(icrmLeadService.getAllLeads(startDate,type,pageable),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/lead/details/download")
    public GenericResponse<Boolean> dowloadLeadDetails(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate, @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size, @RequestParam(value = "type",required = false) String type,@RequestParam() String emailId) throws CustomException {
        icrmLeadService.downloadLeadDetails(startDate,type,emailId);
        return ResponseBuilderUtil.getResponse(true,SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/lead/history/details")
    public GenericResponse<List<LeadHistoryResponseDto>> getLeadHistory(@RequestParam(name = "lead_id") String leadId) throws CustomException {

        return ResponseBuilderUtil.getResponse( icrmLeadService.getLeadHistory(leadId),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/lead-statuses")
    public GenericResponse<List<LeadStatusDTO>> getLeadStatuses() {
        return ResponseBuilderUtil.getResponse(Stream.of(LeadStatus.values())
                .map(status -> new LeadStatusDTO(status.name(), status.getDisplayName()))
                .collect(Collectors.toList()),SUCCESS,
                        REQUEST_SUCCESS, HttpStatus.SC_OK);
    }

    @GetMapping("/lead-events")
    public GenericResponse<Map<String, List<Map<String, String>>>> getLeadEvents() {
        return ResponseBuilderUtil.getResponse(icrmLeadService.getEvent(),SUCCESS,
                REQUEST_SUCCESS, HttpStatus.SC_OK);
    }


}
