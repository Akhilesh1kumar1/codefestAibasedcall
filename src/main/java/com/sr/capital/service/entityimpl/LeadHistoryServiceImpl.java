package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.LeadHistory;
import com.sr.capital.repository.mongo.LeadHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LeadHistoryServiceImpl {

    final LeadHistoryRepository leadHistoryRepository;

    public void saveLeadHistory(LeadHistory leadHistory){
        leadHistoryRepository.save(leadHistory);
    }
}
