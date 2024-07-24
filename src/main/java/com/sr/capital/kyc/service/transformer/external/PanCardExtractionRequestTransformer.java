package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.PanCardExtractionRequest;
import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import com.sr.capital.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class PanCardExtractionRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request)  {

        FileDetails file1 = request.getFile1();
        PanDocDetails docDetails = null;
        try {
            docDetails = MapperUtils.convertValue(request.getDocDetails(), PanDocDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DocumentExtractionData data = DocumentExtractionData.builder()
                .build();



        PanCardExtractionRequest extractionRequest = PanCardExtractionRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data).panNumber(docDetails.getIdNumber())
                .build();

        return (T) extractionRequest;
    }
}
