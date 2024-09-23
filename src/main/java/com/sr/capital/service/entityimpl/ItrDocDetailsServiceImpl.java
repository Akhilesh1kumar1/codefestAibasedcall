package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.kyc.ItrDocDetails;
import com.sr.capital.entity.mongo.kyc.ItrDocDetailsHistory;
import com.sr.capital.repository.mongo.ItrDocDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItrDocDetailsServiceImpl {

    final ItrDocDetailsRepository itrDocDetailsRepository;

    final ItrDocHistoryServiceImpl itrDocDetailsHistoryService;
    public void saveItrDoc(ItrDocDetails itrDocDetails){
        itrDocDetailsRepository.save(itrDocDetails);
        ItrDocDetailsHistory itrDocDetailsHistory =ItrDocDetailsHistory.builder().itrDocDetails(itrDocDetails).srCompanyId(itrDocDetails.getSrCompanyId()).build();
        itrDocDetailsHistoryService.saveItrHistory(itrDocDetailsHistory);
    }

    public void updateItr(ItrDocDetails itrDocDetails){
        ItrDocDetails itrDocDetails1 =itrDocDetailsRepository.findBySrCompanyId(itrDocDetails.getSrCompanyId());
        if(itrDocDetails1!=null){
            itrDocDetails1.setItrExtractionData(itrDocDetails.getItrExtractionData());
            itrDocDetails1.setRequestId(itrDocDetails1.getRequestId());
            itrDocDetailsRepository.save(itrDocDetails1);
        }else
          itrDocDetailsRepository.save(itrDocDetails);

        ItrDocDetailsHistory itrDocDetailsHistory =ItrDocDetailsHistory.builder().itrDocDetails(itrDocDetails).srCompanyId(itrDocDetails.getSrCompanyId()).build();
        itrDocDetailsHistoryService.saveItrHistory(itrDocDetailsHistory);
    }
}
