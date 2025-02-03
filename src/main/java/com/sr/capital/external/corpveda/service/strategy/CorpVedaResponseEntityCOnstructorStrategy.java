package com.sr.capital.external.corpveda.service.strategy;

import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorpVedaResponseEntityCOnstructorStrategy {

    public CorpVedaDocDetails<?> constructEntity(CorpVedaDocOrChestratorRequest request, PartnerDetailsMetaData metaData) {
        if (metaData.getReportStatus().equals("Completed")) {
            return CorpVedaDocDetails.builder()
                    .corpVedaDocType(request.getDocType())
                    .srCompanyId(request.getSrCompanyId())
                    .details(metaData)
                    .initialStatus("Completed")
                    .build();
        } else {
            return CorpVedaDocDetails.builder()
                    .corpVedaDocType(request.getDocType())
                    .srCompanyId(request.getSrCompanyId())
                    .details(request.getCOrpVedaBaseResponse().getData())
                    .initialStatus("Pending")
                    .build();
        }


    }

}
