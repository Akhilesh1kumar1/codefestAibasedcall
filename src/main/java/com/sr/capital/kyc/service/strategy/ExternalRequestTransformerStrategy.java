package com.sr.capital.kyc.service.strategy;


import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.request.IdfyBaseRequest;
import com.sr.capital.kyc.service.interfaces.ExternalRequestTransformer;
import com.sr.capital.kyc.service.transformer.external.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExternalRequestTransformerStrategy {

    @Autowired
    private AadhaarExtractionRequestTransformer aadhaarExtractionRequestTransformer;

    @Autowired
    private BankExtractionRequestTransformer bankExtractionRequestTransformer;

    @Autowired
    private GstExtractionRequestTransformer gstExtractionRequestTransformer;

    @Autowired
    private PanCardExtractionRequestTransformer panCardExtractionRequestTransformer;

    @Autowired
    private AadhaarVerificationRequestTransformer aadhaarVerificationRequestTransformer;

    @Autowired
    private BankVerificationRequestTransformer bankVerificationRequestTransformer;

    @Autowired
    private GstVerificationRequestTransformer gstVerificationRequestTransformer;

    @Autowired
    private PanAadhaarCrossVerificationRequestTransformer panAadhaarCrossVerificationRequestTransformer;

    @Autowired
    private PanCardVerificationRequestTransformer panCardVerificationRequestTransformer;

    @Autowired
    private PanGstCrossVerificationRequestTransformer panGstCrossVerificationRequestTransformer;


    public <T extends IdfyBaseRequest<?>> T transformExtractionRequest(DocOrchestratorRequest request) throws RequestTransformerNotFoundException {
        ExternalRequestTransformer requestTransformer;
        DocType docType = request.getDocType();
        switch (docType) {
            case AADHAAR:
                requestTransformer = aadhaarExtractionRequestTransformer;
                break;
            case BANK_CHEQUE:
                requestTransformer = bankExtractionRequestTransformer;
                break;
            case GST:
                requestTransformer = gstExtractionRequestTransformer;
                break;
            case PAN:
                requestTransformer = panCardExtractionRequestTransformer;
                break;
            default:
                throw new RequestTransformerNotFoundException();
        }

        return requestTransformer.transformRequest(request);
    }


    public <T extends IdfyBaseRequest<?>> T transformVerificationRequest(DocOrchestratorRequest request) throws RequestTransformerNotFoundException {
        ExternalRequestTransformer requestTransformer;
        TaskType taskType = request.getTask().getType();
        switch (taskType) {
            case AADHAAR:
                requestTransformer = aadhaarVerificationRequestTransformer;
                break;
            case PAN:
                requestTransformer = panCardVerificationRequestTransformer;
                break;
            case GST:
                requestTransformer = gstVerificationRequestTransformer;
                break;
            case BANK_DETAILS:
                requestTransformer = bankVerificationRequestTransformer;
                break;
            case PAN_GST:
                requestTransformer = panGstCrossVerificationRequestTransformer;
                break;
            case PAN_AADHAAR:
                requestTransformer = panAadhaarCrossVerificationRequestTransformer;
                break;
            case PAN_BANK_DETAILS:
                return null;
            default:
                throw new RequestTransformerNotFoundException();
        }

        return requestTransformer.transformRequest(request);
    }
}
