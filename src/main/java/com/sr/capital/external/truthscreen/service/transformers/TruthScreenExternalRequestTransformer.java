package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;

public interface TruthScreenExternalRequestTransformer {

    <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request);

}
