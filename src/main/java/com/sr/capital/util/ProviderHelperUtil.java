package com.sr.capital.util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.spine.JsonPathEvaluator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.ERROR;
import static com.omunify.restutil.RestUtilProvider.getInstance;
import static com.sr.capital.helpers.constants.ProviderServiceConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProviderHelperUtil {

    final JsonPathEvaluator jsonPathEvaluator;

    final ProviderConfigUtil providerConfigUtil;

    public HttpResponse makeApiCall(Map<String,Object> params, String url, Object requestBody, Class responseClass) throws UnirestException, URISyntaxException {

        HttpResponse restResponseEntity=null;
        String method = GET;

        if(responseClass == null)
            responseClass = Object.class;

        if(params.containsKey(METHOD)){
            method = (String) params.get(METHOD);
        }else if(params.containsKey(ProviderUrlConfigTypes.OTHER.name())){
            HashMap otherParams = (HashMap) params.get(ProviderUrlConfigTypes.OTHER.name());
            if(otherParams.containsKey(METHOD)){
                method = (String) otherParams.get(METHOD);
            }
        }

         url = getUrlWithQueryparam(url, (Map<String, Object>) params.get(ProviderUrlConfigTypes.QUERY_PARAM.name()));
        if (method != null && !method.isBlank()) {

            switch (method.toLowerCase()){
                case GET:
                    restResponseEntity = getInstance().withHeaders( (Map<String, String>) params.get(ProviderUrlConfigTypes.HEADER.name())).get(url, responseClass);
                    break;
                case POST:
                    restResponseEntity = getInstance().withHeaders( (Map<String, String>) params.get(ProviderUrlConfigTypes.HEADER.name())).post(url, requestBody, responseClass);
                    break;
                case PUT:
                    restResponseEntity = getInstance().withHeaders( (Map<String, String>) params.get(ProviderUrlConfigTypes.HEADER.name())).put(url, requestBody, responseClass);

            }
        } else {
            restResponseEntity = getInstance().withHeaders( (Map<String, String>) params.get(ProviderUrlConfigTypes.HEADER.name())).get(url, responseClass);

        }

      /*  if(restResponseEntity!=null && restResponseEntity.getStatusCode()==HttpStatus.MOVED_PERMANENTLY){
            url= restResponseEntity.getHeaders()!=null && restResponseEntity.getHeaders().getLocation()!=null ? String.valueOf(restResponseEntity.getHeaders().getLocation()) :null;
            if(url==null){
                return restResponseEntity;
            }
            if (!params.containsKey(ServiceConstants.METHOD) || params.get(ServiceConstants.METHOD).toString().equalsIgnoreCase(ServiceConstants.POST)) {
                restResponseEntity = commonConnector.postRequest(url, (Map<String, String>) params.get(ChannelConfigTypes.HEADER.name()), (Map<String, ?>) params.get(ChannelConfigTypes.QUERY_PARAM.name()), requestBody, responseClass);
            } else {
                restResponseEntity = commonConnector.putRequest(url, (Map<String, String>) params.get(ChannelConfigTypes.HEADER.name()), (Map<String, ?>) params.get(ChannelConfigTypes.QUERY_PARAM.name()), requestBody, responseClass);

            }
        }*/
        if(responseClass == String.class) {
            try {
                HashMap<String, Object> responseData =MapperUtils.readValue(restResponseEntity.getBody().toString().replaceAll("[\r\n]+", " "), HashMap.class);
                //restResponseEntity = new ResponseEntity<>(responseData, getHeaderMap(restResponseEntity), HttpStatusCode.valueOf(restResponseEntity.getStatus()));
            }catch(Exception ex){
                log.info("Exception occurred while processing the the Response body");
            }
        }

        return restResponseEntity;
    }


    private String getUrlWithQueryparam(String url ,Map<String,Object> queryParam) throws URISyntaxException {
        URIBuilder uri = new URIBuilder(url) ;
        if(queryParam!=null){
            queryParam.forEach((k,v)->{
                uri.addParameter(k, String.valueOf(v));
            });
        }

        return uri.toString();
    }

    private Map<String,String> getHeaderMap(HttpResponse response){
        Map<String,String> headersMap =new HashMap<>();
        if(response.getHeaders()!=null){
            response.getHeaders().forEach((key, value) -> headersMap.put(key, String.join(",", value)));

        }
        return headersMap;
    }

    public void setResponse(GenericResponse response, HttpResponse restResponseEntity, String group, Long partnerId){


        if(restResponseEntity!=null &&  restResponseEntity.getBody()!=null){

            Map<String,Object> template = providerConfigUtil.getProviderTemplates(restResponseEntity.getBody(),group,partnerId,true);
            Object result=null;
            if(template!=null){
                result = jsonPathEvaluator.evaluate(template,restResponseEntity.getBody());
            }

            response.setData(result);
            response.setStatus(Constants.StatusEnum.SUCCESS);
            response.setStatusCode(restResponseEntity.getStatus());
        }else if(restResponseEntity!=null && (restResponseEntity.getStatus()==HttpStatus.OK.value())){
            log.info("Response from channel : {} ",restResponseEntity);
            response.setStatusCode(restResponseEntity.getStatus());
            return;
        }else{
            log.info("Response from channel : {} ",restResponseEntity);
            response.setStatus(ERROR);
            response.setStatusCode(restResponseEntity.getStatus());
        }
    }
}
