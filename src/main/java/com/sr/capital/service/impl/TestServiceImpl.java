package com.sr.capital.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import com.sr.capital.helpers.enums.ProviderResponseTemplateType;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.spine.JsonPathEvaluator;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.ProviderConfigUtil;
import com.sr.capital.util.ProviderHelperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl {

    final JsonPathEvaluator jsonPathEvaluator;
    final ProviderHelperUtil providerHelperUtil;
    final ProviderConfigUtil providerConfigUtil;
    public boolean testJsonPath(Long partnerId, Map<String,String> metaData, Map<String,Object> request) throws UnirestException, URISyntaxException, IOException {

        Map<String, Object> params = providerConfigUtil.getUrlAndQueryParam(partnerId,metaData, ProviderRequestTemplateType.VALIDATE_TOKEN.name());
        if (params == null ) {
            return true;
        }

        Object requestBody = null;

        Map<String, Object> template = providerConfigUtil.getProviderTemplates(metaData, ProviderRequestTemplateType.VALIDATE_TOKEN.name(), partnerId, true);

        if(template!=null){
            requestBody = jsonPathEvaluator.evaluate(template, request);
        }
        HttpResponse restResponseEntity =null;
        restResponseEntity = providerHelperUtil.makeApiCall(params, (String) params.getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(),""),requestBody,null);
        GenericResponse response =new GenericResponse();
        providerHelperUtil.setResponse(response,restResponseEntity, ProviderResponseTemplateType.VALIDATE_TOKEN_RESPONSE.name(),partnerId);
        ValidateTokenResponse response1 = MapperUtils.convertValue(response.getData(),ValidateTokenResponse.class);
        log.info("response code {} ",response1.getCode());
        return true;
    }
}
