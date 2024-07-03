package com.sr.capital.kyc.service.transformer.external;

import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.GstDetailsByPanExtractionRequest;
import com.sr.capital.kyc.external.request.GstSearchByPanRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class GstDetailsByPanCardRequestTransformer implements ExternalRequestTransformer {


    @Override
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request)  {

        GstSearchByPanRequest requestData = null;
        try {
            requestData = MapperUtils.convertValue( request.getDocDetails(), GstSearchByPanRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (T) GstDetailsByPanExtractionRequest.builder().data(requestData).taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString()).build();
    }
}
