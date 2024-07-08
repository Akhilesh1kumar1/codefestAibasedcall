package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.repository.mongo.LeadGenerationRepository;
import com.sr.capital.service.LeadGenerationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeadGenerationServiceImpl implements LeadGenerationService {

    final LeadGenerationRepository leadGenerationRepository;
    @Override
    public GenerateLeadResponseDto saveLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        List<Lead> leadList = leadGenerationRepository.findBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
        if(CollectionUtils.isNotEmpty(leadList)){
            throw new CustomException("Lead is already generated", HttpStatus.BAD_REQUEST);
        }
        Lead lead =Lead.builder().srCompanyId(Long.valueOf(RequestData.getTenantId())).amount(generateLeadRequestDto.getAmount()).duration(generateLeadRequestDto.getDuration()).status(generateLeadRequestDto.getStatus()).build();
        leadGenerationRepository.save(lead);
        return GenerateLeadResponseDto.builder().id(lead.getId()).build();
    }

    @Override
    public List<GenerateLeadResponseDto> getAllLeads(Long srCompanyId) {
        List<Lead> leadList = leadGenerationRepository.findBySrCompanyId(srCompanyId);
        List<GenerateLeadResponseDto> responseDtos =new ArrayList<>();
        leadList.forEach(lead -> {
            responseDtos.add(GenerateLeadResponseDto.builder().status(lead.getStatus()).amount(lead.getAmount()).duration(lead.getDuration()).id(lead.getId()).build());
        });
        return responseDtos;
    }

    @Override
    public GenerateLeadResponseDto updateLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        if(generateLeadRequestDto.getLeadId()==null){
            throw new CustomException("Lead_id cannot be null", HttpStatus.BAD_REQUEST);

        }

        Lead lead =leadGenerationRepository.findById(generateLeadRequestDto.getLeadId()).orElse(null);
        if(lead==null){
            throw new CustomException("lead details not found", HttpStatus.BAD_REQUEST);

        }

        if(lead.getSrCompanyId().longValue()!=Long.valueOf(RequestData.getTenantId()).longValue()){
            throw new CustomException("Invalid Lead Id", HttpStatus.BAD_REQUEST);

        }

        lead.setStatus(generateLeadRequestDto.getStatus());
        leadGenerationRepository.save(lead);

        return GenerateLeadResponseDto.builder().status(lead.getStatus()).id(lead.getId()).build();
    }
}
