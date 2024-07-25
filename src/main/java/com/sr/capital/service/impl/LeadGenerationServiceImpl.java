package com.sr.capital.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.GenerateLeadRequestDto;
import com.sr.capital.dto.response.GenerateLeadResponseDto;
import com.sr.capital.dto.response.event.Action;
import com.sr.capital.dto.response.event.Events;
import com.sr.capital.dto.response.event.Transitions;
import com.sr.capital.entity.mongo.Lead;
import com.sr.capital.entity.mongo.LeadHistory;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.CustomServiceException;
import com.sr.capital.helpers.enums.LeadStatus;
import com.sr.capital.repository.mongo.LeadGenerationRepository;
import com.sr.capital.service.LeadGenerationService;
import com.sr.capital.service.entityimpl.LeadHistoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class LeadGenerationServiceImpl implements LeadGenerationService {

    final LeadGenerationRepository leadGenerationRepository;
    final LeadHistoryServiceImpl leadHistoryService;

    @Autowired
    @Qualifier("leadEvents")
    Events events;
    @Override
    public GenerateLeadResponseDto saveLead(GenerateLeadRequestDto generateLeadRequestDto) throws CustomException {
        List<Lead> leadList = leadGenerationRepository.findBySrCompanyId(Long.valueOf(RequestData.getTenantId()));
        if(CollectionUtils.isNotEmpty(leadList)){
            throw new CustomException("Lead is already generated", HttpStatus.BAD_REQUEST);
        }
        Lead lead =Lead.builder().srCompanyId(generateLeadRequestDto.getSrCompanyId()!=null? generateLeadRequestDto.getSrCompanyId() : Long.valueOf(RequestData.getTenantId())).amount(generateLeadRequestDto.getAmount()).duration(generateLeadRequestDto.getDuration()).leadSource(generateLeadRequestDto.getLeadSource()).status(LeadStatus.LEAD_START).userName(generateLeadRequestDto.getUserName()).build();
        lead.setLastModifiedBy(String.valueOf(RequestData.getUserId()));
        lead.setCreatedBy(String.valueOf(RequestData.getUserId()));
        leadGenerationRepository.save(lead);
        return GenerateLeadResponseDto.builder().id(lead.getId()).build();
    }

    @Override
    public List<GenerateLeadResponseDto> getAllLeads(Long srCompanyId) {
        List<Lead> leadList = leadGenerationRepository.findBySrCompanyId(srCompanyId);
        List<GenerateLeadResponseDto> responseDtos =new ArrayList<>();
        leadList.forEach(lead -> {
            responseDtos.add(GenerateLeadResponseDto.builder().status(lead.getStatus()).amount(lead.getAmount()).duration(lead.getDuration()).id(lead.getId()).tier(lead.getTier()).leadSource(lead.getLeadSource()).loanApplicationId(lead.getLoanApplicationId())
                    .remarks(lead.getRemarks()).loanVendorPartnerId(lead.getLoanVendorPartnerId()).build());
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

       /* if(lead.getSrCompanyId().longValue()!=Long.valueOf(RequestData.getTenantId()).longValue()){
            throw new CustomException("Invalid Lead Id", HttpStatus.BAD_REQUEST);

        }*/
       // LeadStatus.validateLeadStatus(lead.getStatus());
        Transitions tr =new Transitions(lead.getStatus());
        LeadHistory leadHistory =LeadHistory.builder().srCompanyId(lead.getSrCompanyId()).amount(lead.getAmount()).duration(lead.getDuration()).status(lead.getStatus()).leadSource(lead.getLeadSource()).loanApplicationId(lead.getLoanApplicationId())
                .loanVendorPartnerId(lead.getLoanVendorPartnerId()).tier(lead.getTier()).remarks(lead.getRemarks()).leadId(lead.getId()).userName(lead.getUserName()).build();

        leadHistory.setCreatedBy(lead.getCreatedBy());
        leadHistory.setLastModifiedBy(lead.getLastModifiedBy());
        markItemStatusBasedOnEvent(generateLeadRequestDto.getStatus().name(),tr,events,lead);
        leadHistoryService.saveLeadHistory(leadHistory);
        updateLeadDetails(lead,generateLeadRequestDto);
        leadGenerationRepository.save(lead);

        return GenerateLeadResponseDto.builder().status(lead.getStatus()).id(lead.getId()).build();
    }

    @Override
    public Page<Lead> getAllLeads(LocalDateTime dateTime,String type, Pageable pageable) {
        if(type==null || type.equalsIgnoreCase("createdAt")){
            return leadGenerationRepository.findByCreatedAtBetween(dateTime,pageable);
        }else{
            return leadGenerationRepository.findByLastModifiedAtBetween(dateTime,pageable);
        }

    }

    private void updateLeadDetails(Lead lead,GenerateLeadRequestDto generateLeadRequestDto){
       // lead.setLeadSource(generateLeadRequestDto.getLeadSource());
        lead.setAmount(generateLeadRequestDto.getAmount());
        lead.setDuration(generateLeadRequestDto.getDuration());
        lead.setTier(generateLeadRequestDto.getTier());
        lead.setRemarks(generateLeadRequestDto.getRemarks());
        lead.setLoanVendorPartnerId(generateLeadRequestDto.getLoanVendorPartnerId());
       // lead.setStatus(generateLeadRequestDto.getStatus());
        lead.setLastModifiedBy(RequestData.getUserId()!=null?String.valueOf(RequestData.getUserId()):"SYSTEM");
        lead.setUserName(generateLeadRequestDto.getUserName());
        lead.setLastModifiedAt(LocalDateTime.now());

    }


    private  void markItemStatusBasedOnEvent(String operation, Transitions transition, Events events, Lead lead) {
        Action action = events.getEvents().get(operation).getTransition().get(transition);
        Transitions currentTransition=null;
        if(Objects.isNull(action)) {
            //currentTransition = getInitialTransitionAction(operation);
            throw new CustomServiceException("Invalid status: status update from  "+transition.getStatus()+" to "+operation+" is invalid");
        }else
            currentTransition=action.getState();
        lead.setStatus(currentTransition.getStatus());
    }

}
