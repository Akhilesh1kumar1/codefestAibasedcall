package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import org.springframework.stereotype.Service;

@Service
public class TruthScreenPanExtractionRequestTransformer implements TruthScreenExternalRequestTransformer {

    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) {



        return null;
    }
}
