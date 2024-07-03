package com.sr.capital.kyc.service.interfaces;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;

import java.io.IOException;

public interface ExternalRequestTransformer {

    <T extends KarzaBaseRequest<?>> T transformRequest(DocOrchestratorRequest request) ;

}
