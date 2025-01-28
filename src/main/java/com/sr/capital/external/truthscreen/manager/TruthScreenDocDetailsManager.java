package com.sr.capital.external.truthscreen.manager;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetailsHistory;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsHistoryRepository;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class TruthScreenDocDetailsManager {

    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    private final TruthScreenDocDetailsHistoryRepository truthScreenDocDetailsHistoryRepository;

    public TruthScreenDocDetails<?> saveDocs(TruthScreenDocDetails truthScreenDocDetails){
        if (truthScreenDocDetails == null){
            log.error("Doc Details not present for the user with tentant_id " + RequestData.getTenantId());
        }

        truthScreenDocDetailsHistoryRepository.save(TruthScreenDocDetailsHistory.builder().srCompanyId(RequestData.getTenantId()).docDetails(truthScreenDocDetails).build());
        return truthScreenDocDetailsRepository.save(truthScreenDocDetails);
    }

}
