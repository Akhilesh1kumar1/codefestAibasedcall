package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.external.request.GstExtractionRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import com.sr.capital.kyc.external.request.extraction.data.GstExtractionData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class GstExtractionRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {


        GstExtractionData data = null;
        try {
            data = MapperUtils.convertValue(request.getDocDetails(), GstExtractionData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GstExtractionRequest extractionRequest = GstExtractionRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data)
                .build();

        return (T) extractionRequest;
    }
}
