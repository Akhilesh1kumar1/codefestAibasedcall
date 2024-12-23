package com.sr.capital.service.impl;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import com.sr.capital.helpers.enums.ProviderResponseTemplateType;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.spine.JsonPathEvaluator;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.ProviderConfigUtil;
import com.sr.capital.util.ProviderHelperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestServiceImpl {

    final CreditPartnerFactoryService creditPartnerFactoryService;
    final JsonPathEvaluator jsonPathEvaluator;
    final ProviderHelperUtil providerHelperUtil;
    final ProviderConfigUtil providerConfigUtil;
    final RedissonClient redissonClient;

    public boolean testJsonPath(Long partnerId, Map<String,String> metaData, Map<String,Object> request) throws UnirestException, URISyntaxException, IOException, CustomException {

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

    public Object testAccessToken(String partner) throws CustomException {

        return creditPartnerFactoryService.getPartnerService(partner).getAccessToken(partner);
    }

    public Object testAccessToken(String partner,String loanId) throws CustomException {
        LoanMetaDataDto.ValidateLoanData validateLoanData =LoanMetaDataDto.ValidateLoanData.builder().mobileNumber("7823452312").panNumber("EBXPP9720A").build();
        LoanMetaDataDto loanMetaDataDto =LoanMetaDataDto.builder().loanVendorId(13l).loanVendorName(partner).loanId(loanId).validateLoanData(validateLoanData).build();
        return  creditPartnerFactoryService.getPartnerService(partner).getLoanDetails(loanMetaDataDto);
    }

    public Object setTempValueInRadis() {
        RMapCache<String, String> crifAccessToken1 = redissonClient.getMapCache("TEST_ENV_VARIABLE_1");
        RMapCache<String, String> crifAccessToken2 = redissonClient.getMapCache("TEST_ENV_VARIABLE_2");
        RMapCache<String, String> crifAccessToken3 = redissonClient.getMapCache("TEST_ENV_VARIABLE_3");
        crifAccessToken1.put("TEST_ENV_VARIABLE_1", "Env var 1", 1, TimeUnit.MINUTES);
        crifAccessToken2.put("TEST_ENV_VARIABLE_2", "Env var 2", 1, TimeUnit.MINUTES);
        crifAccessToken3.put("TEST_ENV_VARIABLE_3", "Env var 3", 1, TimeUnit.MINUTES);

        return null;
    }

    public Object getTempValueInRadis() {
        RMapCache<String, String> crifAccessToken1 = redissonClient.getMapCache("TEST_ENV_VARIABLE_1");
        RMapCache<String, String> crifAccessToken2 = redissonClient.getMapCache("TEST_ENV_VARIABLE_2");
        RMapCache<String, String> crifAccessToken3 = redissonClient.getMapCache("TEST_ENV_VARIABLE_3");
        return Arrays.asList(crifAccessToken1.get("TEST_ENV_VARIABLE_1"), crifAccessToken2.get("TEST_ENV_VARIABLE_2"), crifAccessToken3.get("TEST_ENV_VARIABLE_3" ));
    }
}
