package com.sr.capital.external.truthscreen.service.transformers;

import com.sr.capital.external.truthscreen.dto.data.GstAnalyticsExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.extractions.TruthScreenGstAnalyticsExtractionRequest;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenGstAnalyticsRequestTransformer implements TruthScreenExternalRequestTransformer {
    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        GstAnalyticsExtractionRequestData gstAnalyticsExtractionData = GstAnalyticsExtractionRequestData.builder().transID(request.getSrCompanyId() + "_" + HashUtil.generateRandomId()).docType(request.getDocType().getValue()).docNumber(request.getDocNumber()).gstPortalUsername(request.getGstPortalUsername()).toDate(request.getFromDate()).fromDate(request.getFromDate()).build();
        return (T) TruthScreenGstAnalyticsExtractionRequest.builder().details(gstAnalyticsExtractionData).build();
    }
}
