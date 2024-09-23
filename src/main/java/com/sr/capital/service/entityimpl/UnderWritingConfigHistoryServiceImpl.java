package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.UnderWritingConfigHistory;
import com.sr.capital.repository.mongo.UnderWritingConfigHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnderWritingConfigHistoryServiceImpl {

    final UnderWritingConfigHistoryRepository underWritingConfigHistoryRepository;

    public UnderWritingConfigHistory saveUnderWritingConfigHistory(UnderWritingConfigHistory underWritingConfigHistory){
        return underWritingConfigHistoryRepository.save(underWritingConfigHistory);
    }
}
