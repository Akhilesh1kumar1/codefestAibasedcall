package com.sr.capital.external.corpveda.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;
import com.sr.capital.external.corpveda.entity.PartnerFreeDataDetails;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaEntityTransformer;
import com.sr.capital.external.corpveda.service.transformer.impl.CorpVedaGetDataEntityTransformer;
import com.sr.capital.external.corpveda.service.transformer.impl.CorpVedaPlaceOrderEntityTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CorpVedaEntityConstructorStrategy {


    private final CorpVedaPlaceOrderEntityTransformer placeOrderTransformer;


    private final CorpVedaGetDataEntityTransformer getDataTransformer;

    public <T> T constructEntity(CorpVedaDocOrChestratorRequest request,T entity, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        CorpVedaEntityTransformer entityTransformer;
        if (responseClass.equals(PartnerFreeDataDetails.class)){
            entityTransformer = placeOrderTransformer;
        } else if (responseClass.equals(PartnerDetailsMetaData.class)) {
            entityTransformer = getDataTransformer;
        } else {
            throw new EntityConstructorNotFoundException();
        }
        return entityTransformer.constructEntity(request,entity);
    }


}
