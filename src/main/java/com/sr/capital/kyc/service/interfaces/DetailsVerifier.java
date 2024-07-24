package com.sr.capital.kyc.service.interfaces;


import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.exception.custom.TaskProcessingException;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.exception.KarzaNameComparisonException;

public interface DetailsVerifier {

    VerifierResponse verify(DocOrchestratorRequest orchestratorRequest)
            throws KarzaNameComparisonException, ServiceEndpointNotFoundException, TaskProcessingException;
}
