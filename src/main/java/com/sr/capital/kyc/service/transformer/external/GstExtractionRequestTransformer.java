package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.external.request.GstExtractionRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class GstExtractionRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        FileDetails file1 = request.getFile1();

        DocumentExtractionData data = DocumentExtractionData.builder()
                .document1(file1.getPreSignedUri())
                .build();

        GstExtractionRequest extractionRequest = GstExtractionRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data)
                .build();

        return (T) extractionRequest;
    }
}
