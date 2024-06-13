package com.sr.capital.kyc.external.adaptor;


import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.kyc.external.exception.IDfyExtractionException;
import com.sr.capital.kyc.external.request.*;
import com.sr.capital.kyc.external.response.IdfyBaseResponse;
import com.sr.capital.kyc.external.response.extraction.AadhaarExtractionResponse;
import com.sr.capital.kyc.external.response.extraction.BankExtractionResponse;
import com.sr.capital.kyc.external.response.extraction.GstExtractionResponse;
import com.sr.capital.kyc.external.response.extraction.PanCardExtractionResponse;
import com.sr.capital.kyc.external.utill.IdfyUtil;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class IdfyExtractionAdapter {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private IdfyUtil idfyUtil;

    private final LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(IdfyExtractionAdapter.class);
    @SuppressWarnings("unchecked")
    public <T extends IdfyBaseRequest<?>,
            U extends IdfyBaseResponse<?>> U extractDocumentDetails (final T request)
        throws IDfyExtractionException, ServiceEndpointNotFoundException {

        ExternalRequestMetaData requestMetaData = getRequestEndPointAndDocType(request);

        try {
            return (U) webClientUtil.makeExternalCallBlocking(ServiceName.IDfy,
                kycAppProperties.getIdfyBaseUri(), requestMetaData.getEndpoint(), HttpMethod.POST,"test",
                idfyUtil.getIDfyHeader(), null, request, requestMetaData.getResponseClass());
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new IDfyExtractionException(requestMetaData.getDocType().name());
        }
    }

    private <T extends IdfyBaseRequest<?>> ExternalRequestMetaData getRequestEndPointAndDocType(final T request)
        throws ServiceEndpointNotFoundException {
        if (request instanceof GstExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getIdfyExtractGSTDetailsEndpoint())
                    .docType(DocType.GST)
                    .responseClass(GstExtractionResponse.class)
                    .build();
        } else if (request instanceof PanCardExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getIdfyExtractPancardDetailsEndpoint())
                    .docType(DocType.PAN)
                    .responseClass(PanCardExtractionResponse.class)
                    .build();
        } else if (request instanceof AadhaarExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getIdfyExtractAadhaarDetailsEndpoint())
                    .docType(DocType.AADHAAR)
                    .responseClass(AadhaarExtractionResponse.class)
                    .build();
        } else if (request instanceof BankExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getIdfyExtractBankDetailsEndpoint())
                    .docType(DocType.BANK_CHEQUE)
                    .responseClass(BankExtractionResponse.class)
                    .build();
        } else {
            throw new ServiceEndpointNotFoundException();
        }
    }

}
