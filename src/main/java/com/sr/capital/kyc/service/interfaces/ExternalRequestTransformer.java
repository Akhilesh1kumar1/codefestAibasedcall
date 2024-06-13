package com.sr.capital.kyc.service.interfaces;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;

public interface ExternalRequestTransformer {

    <T extends IdfyBaseRequest<?>> T transformRequest(DocOrchestratorRequest request);

}
