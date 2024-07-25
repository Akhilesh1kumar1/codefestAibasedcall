package com.sr.capital.service;

import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.request.IcrmLeadRequestDto;
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

    public IcrmLoanResponseDto getLoanDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException;

    public IcrmLoanResponseDto getCompleteLoanDetails(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException;

    public GenerateLeadResponseDto updateLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException;

    public LeadDetailsResponseDto getAllLeads(LocalDateTime dateTime,String type, Pageable pageable);


    public void downloadLoanReport(IcrmLeadRequestDto icrmLeadRequestDto) throws CustomException, ParseException, IOException;

    public void downloadLeadDetails(LocalDateTime dateTime,String type,String emailId);

    public List<LeadHistoryResponseDto> getLeadHistory(String leadId);

    public Map<LeadStatus, List<LeadStatus>> getEvent();
}
