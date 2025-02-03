package com.sr.capital.external.corpveda.service.transformer;

import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;

public interface CorpVedaRequestTransformer {

    <T> T transformRequest(CorpVedaDocOrChestratorRequest request) throws Exception;
}
