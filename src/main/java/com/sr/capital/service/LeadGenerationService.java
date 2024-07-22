package com.sr.capital.service;

import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.exception.custom.CustomException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface LeadGenerationService {

    GenerateLeadResponseDto saveLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException;

    List<GenerateLeadResponseDto> getAllLeads(Long srCompanyId);

    GenerateLeadResponseDto updateLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException;

    Page<Lead> getAllLeads(LocalDateTime dateTime,String type ,Pageable pageable);

}
