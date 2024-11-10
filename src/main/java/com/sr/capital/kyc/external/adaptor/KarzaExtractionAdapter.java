package com.sr.capital.kyc.external.adaptor;


import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.kyc.external.exception.KarzaExtractionException;
import com.sr.capital.kyc.external.request.*;
import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.extraction.*;
import com.sr.capital.kyc.external.response.extraction.data.GstDetailsByPanResponseData;
import com.sr.capital.kyc.external.response.extraction.data.GstExtractionResponseData;
import com.sr.capital.kyc.external.response.extraction.data.ItrAdditionalResponseData;
import com.sr.capital.kyc.external.response.verification.VerifyGstOtpResponse;
import com.sr.capital.kyc.external.utill.KarzaUtil;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class KarzaExtractionAdapter {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private KarzaUtil karzaUtil;

    private final LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(KarzaExtractionAdapter.class);
    @SuppressWarnings("unchecked")
    public <T extends KarzaBaseRequest<?>,
            U extends KarzaBaseResponse<?>> U extractDocumentDetails (final T request)
        throws KarzaExtractionException, ServiceEndpointNotFoundException {

        ExternalRequestMetaData requestMetaData = getRequestEndPointAndDocType(request);

        try {
            if(!kycAppProperties.getKarzaEnabled()){
                return createDummyResponse(requestMetaData);
            }
            return (U) webClientUtil.makeExternalCallBlocking(ServiceName.KARZA,
                kycAppProperties.getKarzaBaseUri(), requestMetaData.getEndpoint(), HttpMethod.POST,"test",
                karzaUtil.getKarzaHeader(), null, request.getData(), requestMetaData.getResponseClass());
        } catch (Exception exception) {
            LoggerUtil.error(exception.getMessage());
            throw new KarzaExtractionException(requestMetaData.getDocType().name());
        }
    }

    private <T extends KarzaBaseRequest<?>,
            U extends KarzaBaseResponse<?>> U createDummyResponse(ExternalRequestMetaData requestMetaData) {
        if(requestMetaData.getDocType().equals(DocType.GST_BY_PAN))
            return (U) createDummyGstExtractionResponse();

        if(requestMetaData.getDocType().equals(DocType.GST)){
            return (U) createDummyGstOtpResponse();
        }

        if(requestMetaData.getDocType().equals(DocType.VERIFY_OTP)){
            return (U) createDummyGstOtpResponse();
        }
        if(requestMetaData.getDocType().equals(DocType.ITR)){
            return (U) createItrData();
        }

        return null;
    }

    private Object createDummyGstOtpResponse() {
        GstExtractionResponse response =new GstExtractionResponse();
        GstExtractionResponseData gstExtractionResponseData =new GstExtractionResponseData();
        gstExtractionResponseData.setStatusCd("ok");

        response.setResult(gstExtractionResponseData);
        response.setAction("dummyAction");
        response.setStatusMessage("Dummy Status Message");
        response.setType("dummyType");
        response.setTaskId("dummyTaskId");
        response.setGroupId("dummyGroupId");
        response.setRequestId("dummyRequestId");
        response.setCompletedAt("2023-06-01T12:34:56");
        response.setCreatedAt("2023-06-01T12:00:00");
        response.setError(null);
        response.setMessage("Dummy Message");
        response.setStatusCode(200);
        return response;
    }

    private <T extends KarzaBaseRequest<?>> ExternalRequestMetaData getRequestEndPointAndDocType(final T request)
        throws ServiceEndpointNotFoundException {
       if(request instanceof GstDetailsByPanExtractionRequest) {
           return ExternalRequestMetaData.builder()
                   .endpoint(kycAppProperties.getKarzaExtractGSTDetailsByPanEndpoint())
                   .docType(DocType.GST_BY_PAN)
                   .responseClass(GstDetailsByPanResponse.class)
                   .build();
       }else if(request instanceof VerifyGstExtractionRequest){
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getKarzaVerifyGSTOtpEndpoint())
                    .docType(DocType.VERIFY_OTP)
                    .responseClass(VerifyGstOtpResponse.class)
                    .build();
        }else if (request instanceof GstExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getKarzaExtractGSTDetailsEndpoint())
                    .docType(DocType.GST)
                    .responseClass(GstExtractionResponse.class)
                    .build();
        } else if (request instanceof PanCardExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getKarzaExtractPancardDetailsEndpoint())
                    .docType(DocType.PAN)
                    .responseClass(PanCardExtractionResponse.class)
                    .build();
        } else if (request instanceof AadhaarExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getKarzaExtractAadhaarDetailsEndpoint())
                    .docType(DocType.AADHAR)
                    .responseClass(AadhaarExtractionResponse.class)
                    .build();
        } else if (request instanceof BankExtractionRequest) {
            return ExternalRequestMetaData.builder()
                    .endpoint(kycAppProperties.getKarzaExtractBankDetailsEndpoint())
                    .docType(DocType.BANK_CHEQUE)
                    .responseClass(BankExtractionResponse.class)
                    .build();
        }  else if (request instanceof ItrExtractionRequest) {
           return ExternalRequestMetaData.builder()
                   .endpoint(kycAppProperties.getKarzaExtractItrDetailsEndpoint())
                   .docType(DocType.ITR)
                   .responseClass(ItrExtractionResponseData.class)
                   .build();
       }else {
            throw new ServiceEndpointNotFoundException();
        }
    }


    private GstDetailsByPanResponse createDummyGstExtractionResponse() {
        GstDetailsByPanResponse response = new GstDetailsByPanResponse();
        GstDetailsByPanResponseData data1 = new GstDetailsByPanResponseData();
        data1.setAuthStatus("authorized");
        data1.setGstinStatus("Active");
        data1.setApplicationStatus("MIG");
        data1.setEmailId("entity@example.com");
        data1.setGstinId("123456789012345");
        data1.setGstinRefId("ref123");
        data1.setMobNum("1234567890");
        data1.setPan("ABCDE1234F");
        data1.setRegType("V");
        data1.setRegistrationName("Entity Name");
        data1.setTinNumber("TIN123");
        data1.setState("StateName");

        GstDetailsByPanResponseData data2 = new GstDetailsByPanResponseData();
        data2.setAuthStatus("unauthorized");
        data2.setGstinStatus("Inactive");
        data2.setApplicationStatus("DFT");
        data2.setEmailId("entity2@example.com");
        data2.setGstinId("678901234567890");
        data2.setGstinRefId("ref456");
        data2.setMobNum("0987654321");
        data2.setPan("FGHIJ5678K");
        data2.setRegType("S");
        data2.setRegistrationName("Another Entity");
        data2.setTinNumber("TIN456");
        data2.setState("AnotherState");

        List<GstDetailsByPanResponseData> dataList = Arrays.asList(data1, data2);
        response.setResult(dataList);
        response.setAction("dummyAction");
        response.setStatusMessage("Dummy Status Message");
        response.setType("dummyType");
        response.setTaskId("dummyTaskId");
        response.setGroupId("dummyGroupId");
        response.setRequestId("dummyRequestId");
        response.setCompletedAt("2023-06-01T12:34:56");
        response.setCreatedAt("2023-06-01T12:00:00");
        response.setError(null);
        response.setMessage("Dummy Message");
        response.setStatusCode(200);

        return response;
    }


    public ItrExtractionResponseData createItrData(){
        ItrExtractionResponseData itrExtractionData = ItrExtractionResponseData.builder().build();
        ItrAdditionalResponseData itrAdditionalResponseData =ItrAdditionalResponseData.builder().build();
        itrAdditionalResponseData.setAsData(new ArrayList<>());
        ItrAdditionalResponseData.FormDetails formDetails =new ItrAdditionalResponseData.FormDetails();
        formDetails.setFinancialYear("2024-25");
        itrExtractionData.setRequestId("dummmafn");
        itrAdditionalResponseData.setFormDetails(formDetails);
        itrExtractionData.setResult(itrAdditionalResponseData);
      //  itrAdditionalResponseData.setPdfDownloadLink("https://rb.gy/2hlagh");
        return itrExtractionData;
    }

}
