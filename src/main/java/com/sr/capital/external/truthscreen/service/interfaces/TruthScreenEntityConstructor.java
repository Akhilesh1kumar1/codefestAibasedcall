package com.sr.capital.external.truthscreen.service.interfaces;

import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;

import java.io.IOException;

public interface TruthScreenEntityConstructor {
    <T> T constructEntity(TruthScreenDocDetails<T> request, T entity);
}
