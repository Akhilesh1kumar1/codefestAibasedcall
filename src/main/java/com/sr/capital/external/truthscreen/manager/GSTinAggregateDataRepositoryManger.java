package com.sr.capital.external.truthscreen.manager;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.entity.GSTinDetails;
import com.sr.capital.external.truthscreen.entity.GSTinDetailsHistory;
import com.sr.capital.external.truthscreen.repository.GstinAggregateDataHistoryRepository;
import com.sr.capital.external.truthscreen.repository.GstinAggregateDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class GSTinAggregateDataRepositoryManger {

    private final GstinAggregateDataHistoryRepository gstinAggregateDataHistoryRepository;

    private final GstinAggregateDataRepository gstinAggregateDataRepository;

    public GSTinDetails saveDoc(GSTinDetails gsTinDetails){
        if (gsTinDetails == null){
            log.error("Doc Details not present for the user with tentant_id " + RequestData.getTenantId());
        }

        gstinAggregateDataHistoryRepository.save(GSTinDetailsHistory.builder().transId(gsTinDetails.getTransId()).gsTinDetails(gsTinDetails).build());
        return gstinAggregateDataRepository.save(gsTinDetails);
    }
}
