package com.sr.capital.kyc.service.interfaces;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;

import java.io.IOException;

public interface EntityConstructor {

    <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException;

}