package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.kyc.ItrDocDetailsHistory;
import com.sr.capital.repository.mongo.ItrDocHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItrDocHistoryServiceImpl {

    final ItrDocHistoryRepository itrDocHistoryRepository;

    public void saveItrHistory(ItrDocDetailsHistory itrDocDetailsHistory){
        itrDocHistoryRepository.save(itrDocDetailsHistory);
    }
}
