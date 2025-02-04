package com.sr.capital.external.truthscreen.service.transformers;


import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
@RequiredArgsConstructor
public class TruthScreenExternalRequestTransformerStrategy {


    private final TruthScreenCinRequestTransformer cinRequestTransformer;

    private final TruthScreenGSTinRequestTransformer gstinRequestTransformer;

    private final TruthScreenPanToGstRequestTransformer panToGstRequestTransformer;

    private final TruthScreenPanComplianceRequestTransformer panComplianceRequestTransformer;

    private final TruthScreenPanComprehensiveRequestTransformer panComprehensiveRequestTransformer;

    private final TruthScreenPanExtractionRequestTransformer panExtractionRequestTransformer;

    private final TruthScreenNDDExtractionRequestTransformer nddTransformer;

    private final TruthScreenGstAnalyticsRequestTransformer gstAnalyticsTransformer;

    public <T extends TruthScreenBaseRequest<?>> T transformExtractionRequest(TruthScreenDocOrchestratorRequest request) throws RequestTransformerNotFoundException {
        TruthScreenExternalRequestTransformer requestTransformer;
        TruthScreenDocType docType = request.getDocType();
        switch (docType) {
            case PAN:
                requestTransformer = panExtractionRequestTransformer;
                break;
            case PAN_COMPREHENSIVE:
                requestTransformer = panComprehensiveRequestTransformer;
                break;
            case PAN_COMPLIANCE:
                requestTransformer = panComplianceRequestTransformer;
                break;
            case PAN_TO_GST:
                requestTransformer = panToGstRequestTransformer;
                break;
            case GSTIN:
                requestTransformer = gstinRequestTransformer;
                break;
            case CIN:
                requestTransformer = cinRequestTransformer;
                break;
            case GST_ANALYTICS:
                requestTransformer = gstAnalyticsTransformer;
                break;
            case NDD:
                requestTransformer = nddTransformer;
                break;
            default:
                throw new RequestTransformerNotFoundException();
        }

        try {
            return requestTransformer.transformRequest(request);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (NoSuchProviderException e) {
            throw new RuntimeException(e);
        }
    }

}
