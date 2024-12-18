package com.sr.capital.external.truthscreen.service.transformers;


import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import org.springframework.beans.factory.annotation.Autowired;

public class TruthScreenExternalRequestTransformerStrategy {

    @Autowired
    private TruthScreenPanExtractionRequestTransformer panExtractionRequestTransformer;

    public <T extends TruthScreenBaseRequest<?>> T transformExtractionRequest(TruthScreenDocOrchestratorRequest request) throws RequestTransformerNotFoundException {
        TruthScreenExternalRequestTransformer requestTransformer;
        TruthScreenDocType docType = request.getDocType();
        switch (docType) {
            case PAN:
                requestTransformer = panExtractionRequestTransformer;
                break;
            default:
                throw new RequestTransformerNotFoundException();
        }

        return requestTransformer.transformRequest(request);
    }

}
