package com.sr.capital.external.client;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.config.AppProperties;
import com.sr.capital.external.dto.request.CommunicationRequestTemp;
import com.sr.capital.external.dto.response.KaleyraResponse;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.mashape.unirest.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static com.omunify.restutil.RestUtilProvider.getInstance;


@Component
public class KaleyraClient {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(KaleyraClient.class);

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClientUtil webClientUtil;




//    public void sendNotification(CommunicationRequest communicationRequest) {
//        try {
//            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
//            formData.add("to", communicationRequest.getTo());
//            formData.add("params", communicationRequest.getMessage());
//            getMandatoryFormDataForWhatsAppCommunication(formData);
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.set("api-key",appProperties.getKaleyraApiKey());
//            headers.set(Constants.KaleyraHeaders.CONTENT_TYPE,Constants.ContentType.MULTIPART_FORM_DATA);
//            webClientUtil.makeExternalCallBlocking(ServiceName.KALEYRA, appProperties.getKaleyraBaseurl(),
//                    appProperties.getKaleyraSendMessageEndPoint(), HttpMethod.POST,
//                    AuthThreadContextUtil.getRequestContext().getRequestId(), headers, null, formData,
//                    KaleyraResponse.class);
//
//        } catch (Exception e) {
//            loggerUtil.stackTrace(e);
//        }
//    }

    public KaleyraResponse sendWhatsAppNotification(CommunicationRequestTemp.WhatsAppCommunicationDTO communicationDTO) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            getMandatoryFormDataForWhatsAppCommunication(formData,communicationDTO.getIsOtpFlow());
            formData.add("to", communicationDTO.getRecipientNo());
            formData.add("template_name", communicationDTO.getTemplate());
            if(communicationDTO.getIsOtpFlow()){
                formData.add("verification_code",StringUtils.join(communicationDTO.getParams(), ","));
            }else {
                formData.add("params", StringUtils.join(communicationDTO.getParams(), ","));
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("api-key",appProperties.getKaleyraApiKey());
            headers.set(Constants.KaleyraHeaders.CONTENT_TYPE,Constants.ContentType.MULTIPART_FORM_DATA);

           return webClientUtil.makeExternalCallBlocking(ServiceName.KALEYRA, appProperties.getKaleyraBaseurl() + "/" + appProperties.getKaleyraSenderId(),
                    appProperties.getKaleyraSendMessageEndPoint(), HttpMethod.POST,
                    null, headers, null, formData,
                    KaleyraResponse.class);
        } catch (Exception ignored) {
              loggerUtil.info("Error in sending whatsapp communication : "+ignored.getMessage());
        }
        return null;
    }


    //@Async
    /*public KaleyraResponse sendWhatsAppNotification(CommunicationRequestTemp.WhatsAppCommunicationDTO communicationDTO) {
        KaleyraResponse response =null;
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            getMandatoryFormDataForWhatsAppCommunication(formData);
            formData.add("to", communicationDTO.getRecipientNo());
            formData.add("template_name", communicationDTO.getTemplate());
            formData.add("params", StringUtils.join(communicationDTO.getParams(), ","));

            Map<String,String> headers = new HashMap<>();
            headers.put("api-key",appProperties.getKaleyraApiKey());
            headers.put(Constants.KaleyraHeaders.CONTENT_TYPE, Constants.ContentType.MULTIPART_FORM_DATA);
             String url =appProperties.getKaleyraBaseurl() + "/" + appProperties.getKaleyraSenderId()+appProperties.getKaleyraSendMessageEndPoint();
            HttpResponse<KaleyraResponse> kaleyraResponseHttpResponse = getInstance().withHeaders(headers).post(url,formData, KaleyraResponse.class);
               response =kaleyraResponseHttpResponse.getBody();

           *//* webClientUtil.makeExternalCallBlocking(ServiceName.KALEYRA, appProperties.getKaleyraBaseurl() + "/" + appProperties.getKaleyraSenderId(),
                    appProperties.getKaleyraSendMessageEndPoint(), HttpMethod.POST,
                    null, headers, null, formData,
                    KaleyraResponse.class);*//*
        } catch (Exception ignored) {

        }
         return response;
    }*/

    private void getMandatoryFormDataForWhatsAppCommunication(MultiValueMap<String, String> formData,Boolean isOtp) {
        formData.add("from", appProperties.getKaleyraFromNo());
        formData.add("channel", appProperties.getKaleyraWhatsappChannel());

        if(isOtp){
            formData.add("type","authenticationtemplate");
        }else{
            formData.add("type", "mediatemplate");
        }
        //TODO: Configure a callback url and then add this.
//        formData.add("callback_url", appProperties.getKaleyraWhatsappCallbackUri());
    }
}
