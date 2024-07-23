package com.sr.capital.external.client;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.CustomServiceException;
import com.sr.capital.external.dto.request.NetCoreSendEmailRequest;
import com.sr.capital.external.dto.response.KaleyraResponse;
import com.sr.capital.external.dto.response.NetCoreSendEmailResponse;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.omunify.restutil.RestUtilProvider.getInstance;
import static com.sr.capital.helpers.enums.ServiceName.NETCORE;
import static org.apache.http.HttpStatus.SC_OK;


@Component
public class NetCoreClient {

    @Autowired
    private AppProperties appProperties;

   LoggerUtil loggerUtil =LoggerUtil.getLogger(NetCoreClient.class);

   @Autowired
   private WebClientUtil webClientUtil;

    public NetCoreSendEmailResponse triggerEmail(NetCoreSendEmailRequest emailRequest) throws UnirestException {
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.add("api_key", appProperties.getNetCoreApiKey());
        return webClientUtil.makeExternalCallBlocking(NETCORE, appProperties.getNetCoreBaseUri(),
                appProperties.getNetCoreSendEmailEndpoint(), HttpMethod.POST, null,
                headers, null, emailRequest, NetCoreSendEmailResponse.class);
        /*String url =appProperties.getNetCoreBaseUri()+ appProperties.getNetCoreSendEmailEndpoint();
        HttpResponse<NetCoreSendEmailResponse> netCoreSendEmailResponseHttpResponse = getInstance().withHeaders(headers).post(url,emailRequest,NetCoreSendEmailResponse.class);

        if (netCoreSendEmailResponseHttpResponse.getStatus() != SC_OK && netCoreSendEmailResponseHttpResponse.getBody() == null) {
            loggerUtil.error("Received response: "+netCoreSendEmailResponseHttpResponse.getBody()+" with status code: "+netCoreSendEmailResponseHttpResponse.getStatus()+" while sending email" );
            throw new CustomServiceException(String.format("Received response: %s with status code: %s while sending " +
                            " email", netCoreSendEmailResponseHttpResponse.getBody(),
                    netCoreSendEmailResponseHttpResponse.getStatus()));
        }
        return netCoreSendEmailResponseHttpResponse.getBody();*/
    }
}
