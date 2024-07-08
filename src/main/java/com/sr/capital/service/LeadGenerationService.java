package com.sr.capital.service;

import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.exception.custom.CustomException;

import java.util.List;

public interface LeadGenerationService {

    GenerateLeadResponseDto saveLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException;

    List<GenerateLeadResponseDto> getAllLeads(Long srCompanyId);
}
