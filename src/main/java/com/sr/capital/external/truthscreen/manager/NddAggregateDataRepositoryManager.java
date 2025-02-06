package com.sr.capital.external.truthscreen.manager;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.entity.GSTinDetails;
import com.sr.capital.external.truthscreen.entity.GSTinDetailsHistory;
import com.sr.capital.external.truthscreen.entity.NddDetails;
import com.sr.capital.external.truthscreen.entity.NddDetailsHistory;
import com.sr.capital.external.truthscreen.repository.NddAggregateDataHistoryRepository;
import com.sr.capital.external.truthscreen.repository.NddAggregateDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NddAggregateDataRepositoryManager {

    private final NddAggregateDataHistoryRepository nddAggregateDataHistoryRepository;

    private final NddAggregateDataRepository nddAggregateDataRepository;

    public NddDetails saveDoc(NddDetails nddDetails){
        if (nddDetails == null){
            log.error("Doc Details not present for the user with tentant_id " + RequestData.getTenantId());
        }

        nddAggregateDataHistoryRepository.save(NddDetailsHistory.builder().srCompanyId(RequestData.getTenantId()).transId(nddDetails.getTransId()).nddDetails(nddDetails).build());
        return nddAggregateDataRepository.save(nddDetails);
    }

}
