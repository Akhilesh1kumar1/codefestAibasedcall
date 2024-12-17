package com.sr.capital.external.truthscreen.adapter;

import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.external.truthscreen.adapter.extractions.PanDetailsExtractionRequest;
import com.sr.capital.external.truthscreen.dto.response.PanResponseDto;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.util.TruthScreenUtil;
import com.sr.capital.external.truthscreen.util.TruthScreenUtility;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

@Component
public class TruthScreenAdapter {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private TruthScreenUtil truthScreenUtil;

    private final com.sr.capital.util.LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(TruthScreenAdapter.class);

    public <T extends TruthScreenBaseRequest<?>, U extends TruthScreenBaseResponse<?>> U extractDetails(final T request) throws Exception {

        String iv = TruthScreenUtility.getIV();
        String encryptedData = TruthScreenUtility.encrypt(appProperties.getAuthBridgePassword(),iv,request);
        TruthScreenExternalRequestMetaData requestMetaData = getRequestEndPointAndDocType(request);

        try{
             String responseDataToDecrypt  = (String) webClientUtil.makeExternalCallBlocking(ServiceName.TRUTHSCREEN,appProperties.getAuthBridgeBaseUrl(),requestMetaData.getEndpoint(), HttpMethod.POST,"test",truthScreenUtil.getHeaders(),null,encryptedData,requestMetaData.getResponseClass());
             return (U) TruthScreenUtility.decrypt(iv,responseDataToDecrypt,requestMetaData.getResponseClass());
        }
        catch (Exception exception){
            LoggerUtil.error(exception.getMessage());
            throw new Exception(requestMetaData.getDocType().getName());
        }

    }

    private <T extends TruthScreenBaseRequest<?>> TruthScreenExternalRequestMetaData getRequestEndPointAndDocType(final T request) throws ServiceEndpointNotFoundException{
        if (request instanceof PanDetailsExtractionRequest){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeBaseUrl()+"api/v2.2/idseach")
                    .docType(TruthScreenDocType.PAN)
                    .responseClass(PanResponseDto.class)
                    .build();
        }
        else {
            throw new ServiceEndpointNotFoundException();
        }
    }
}
