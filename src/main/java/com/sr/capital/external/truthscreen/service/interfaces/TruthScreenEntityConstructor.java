package com.sr.capital.external.truthscreen.service.interfaces;

import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;

import java.io.IOException;

public interface TruthScreenEntityConstructor {
    <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException;
}
