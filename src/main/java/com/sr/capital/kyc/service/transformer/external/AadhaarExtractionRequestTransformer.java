package com.sr.capital.kyc.service.transformer.external;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.external.request.AadhaarExtractionRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.extraction.data.AadhaarExtractionData;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AadhaarExtractionRequestTransformer implements ExternalRequestTransformer {

    @Override
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) {

        FileDetails file1 = request.getFile1();
        FileDetails file2 = request.getFile2();

        AadhaarExtractionData data = AadhaarExtractionData.builder()
                .document1(file1.getPreSignedUri())
                .build();
        if(request.hasFile2()){
            data.setDocument2(file2.getPreSignedUri());
        }

        AadhaarExtractionRequest extractionRequest = AadhaarExtractionRequest.builder()
                .taskId(UUID.randomUUID().toString())
                .groupId(UUID.randomUUID().toString())
                .data(data)
                .build();

        return (T) extractionRequest;
    }
}
