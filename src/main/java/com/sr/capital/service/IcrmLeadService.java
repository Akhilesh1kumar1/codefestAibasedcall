package com.sr.capital.service;

import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.request.IcrmLeadDetailsRequestDto;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
import com.sr.capital.dto.request.IcrmLoanRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.dto.response.IcrmLoanResponseDto;
import com.sr.capital.dto.response.LeadDetailsResponseDto;
import com.sr.capital.dto.response.LeadHistoryResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.LeadStatus;

import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IcrmLeadService {

    public IcrmLoanResponseDto getLoanDetails(IcrmLoanRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException;

    public IcrmLoanResponseDto getCompleteLoanDetails(IcrmLoanRequestDto icrmLeadRequestDto) throws CustomException;

    public GenerateLeadResponseDto updateLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException;

    public LeadDetailsResponseDto getAllLeads(IcrmLeadDetailsRequestDto leadRequestDto,Pageable pageable);


    public void downloadLoanReport(IcrmLoanRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException;

    public void downloadLeadDetails(IcrmLeadDetailsRequestDto icrmLeadDetailsRequestDto);

    public List<LeadHistoryResponseDto> getLeadHistory(String leadId);



    public Map<String, List<Map<String, String>>> getEvent();
}
