package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.kyc.GstDocDetailsHistory;
import com.sr.capital.repository.mongo.GstCompeteDocHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GstCompleteHistoryManager {

    GstCompeteDocHistoryRepository gstCompeteDocHistoryRepository;

    public boolean saveGstHistory(GstDocDetailsHistory gstDocDetailsHistory){
        gstCompeteDocHistoryRepository.save(gstDocDetailsHistory);
        return true;
    }
}
