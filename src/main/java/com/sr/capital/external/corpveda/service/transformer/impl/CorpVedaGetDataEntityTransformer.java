package com.sr.capital.external.corpveda.service.transformer.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.PlaceOrderServiceResponseData;
import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;
import com.sr.capital.external.corpveda.repository.PartnerDetailsMetaDataRepository;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaEntityTransformer;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CorpVedaGetDataEntityTransformer implements CorpVedaEntityTransformer {

    private final PartnerDetailsMetaDataRepository partnerDetailsMetaDataRepository;

    @Override
    public <T> T constructEntity(CorpVedaDocOrChestratorRequest request, T entity) throws IOException {
        PlaceOrderServiceResponseData placeOrderServiceResponseData = MapperUtils.convertValue(request.getCOrpVedaBaseResponse(), PlaceOrderServiceResponseData.class);
        PartnerDetailsMetaData partnerDetailsMetaData = MapperUtils.convertValue(placeOrderServiceResponseData, PartnerDetailsMetaData.class);
        partnerDetailsMetaData.setSrCompanyId(request.getSrCompanyId());
        partnerDetailsMetaDataRepository.save(partnerDetailsMetaData);
        return (T) CorpVedaDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .details(partnerDetailsMetaData)
                .corpVedaDocType(request.getDocType())
                .build();
    }
}
