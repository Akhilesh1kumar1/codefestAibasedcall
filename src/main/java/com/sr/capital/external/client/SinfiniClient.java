package com.sr.capital.external.client;


import com.sr.capital.config.AppProperties;
import com.sr.capital.external.dto.request.CommunicationRequestTemp;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



@Component
public class SinfiniClient {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(SinfiniClient.class);

    @Autowired
    AppProperties appProperties;

   @Autowired
    WebClientUtil webClientUtil;

    @Async
    public void sendSmsNotification(CommunicationRequestTemp.SmsCommunicationDTO communicationDTO) {
        try {
            Map<String, String> queryParams = new HashMap<>();
            getMandatoryParametersForSmsCommunication(queryParams);
            queryParams.put("to", communicationDTO.getRecipientNo());
            queryParams.put("template_id", communicationDTO.getTemplate());
            queryParams.put("message", communicationDTO.getBody());

            webClientUtil.makeExternalCallBlocking(ServiceName.SINFINI, appProperties.getSinfiniBaseurl(), null, HttpMethod.GET,
                    "AuthThreadContextUtil.getRequestContext().getRequestId()", null, queryParams,null,
                    String.class);
        } catch (Exception ignored) {

        }

    }

    private void getMandatoryParametersForSmsCommunication(Map<String, String> params) {
        params.put("sender", appProperties.getSinfiniSmsSender());
        params.put("api_key", appProperties.getSinfiniApiKey());
        params.put("method", appProperties.getSinfiniSmsMethod());
        params.put("format", "json");
    }
}
