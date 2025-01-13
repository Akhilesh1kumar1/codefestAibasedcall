package com.sr.capital.external.truthscreen.service.transformers;


import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
public class TruthScreenExternalRequestTransformerStrategy {

    @Autowired
    private TruthScreenCinRequestTransformer cinRequestTransformer;

    @Autowired
    private TruthScreenGSTinRequestTransformer gstinRequestTransformer;

    @Autowired
    private TruthScreenPanToGstRequestTransformer panToGstRequestTransformer;

    @Autowired
    private TruthScreenPanComplianceRequestTransformer panComplianceRequestTransformer;

    @Autowired
    private TruthScreenPanComprehensiveRequestTransformer panComprehensiveRequestTransformer;

    @Autowired
    private TruthScreenPanExtractionRequestTransformer panExtractionRequestTransformer;

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
