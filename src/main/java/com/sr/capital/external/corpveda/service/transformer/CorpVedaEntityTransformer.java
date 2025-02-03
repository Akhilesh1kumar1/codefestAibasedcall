package com.sr.capital.external.corpveda.service.transformer;

import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;

import java.io.IOException;

public interface CorpVedaEntityTransformer {

    <T> T constructEntity(CorpVedaDocOrChestratorRequest request, T entity) throws IOException;

}
