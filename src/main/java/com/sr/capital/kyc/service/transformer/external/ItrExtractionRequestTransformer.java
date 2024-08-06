package com.sr.capital.kyc.service.transformer.external;

import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.GstExtractionRequest;
import com.sr.capital.kyc.external.request.ItrExtractionRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.extraction.data.GstExtractionData;
import com.sr.capital.kyc.external.request.extraction.data.ItrExtractionData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ItrExtractionRequestTransformer  implements ExternalRequestTransformer {
    @Override
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {
        ItrExtractionData data = null;
        try {
            data = MapperUtils.convertValue(request.getDocDetails(), ItrExtractionData.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ItrExtractionRequest extractionRequest = ItrExtractionRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data)
                .build();

        return (T) extractionRequest;
    }
}
