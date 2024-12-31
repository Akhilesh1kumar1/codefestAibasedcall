package com.sr.capital.external.truthscreen.service.transformers.interfaces;

import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public interface TruthScreenExternalRequestTransformer {

    <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException;

}
