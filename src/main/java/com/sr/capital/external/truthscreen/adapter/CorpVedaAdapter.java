package com.sr.capital.external.truthscreen.adapter;

import com.sr.capital.config.AppProperties;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.truthscreen.dto.data.CorpVedaLoginRequestData;
import com.sr.capital.external.truthscreen.dto.data.CorpVedaLoginResponseData;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.WebClientUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class CorpVedaAdapter {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private RedissonClient redissonClient;

    private final com.sr.capital.util.LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(CorpVedaAdapter.class);

    public <T extends CorpVedaBaseRequest<?>, U extends CorpVedaBaseResponse<?>> U extractDetails(final CorpVedaDocOrChestratorRequest request) throws Exception{
        LoggerUtil.info("CorpVedaAdapter.extractDetails() started");
        try {
            String getRedisToken = getAccessToken();
            //2nd API integration

            //3rd API integration

            return null;
        }
        catch (Exception e){
            LoggerUtil.error("CorpVedaAdapter.extractDetails() failed with exception: "+e.getMessage());
            throw e;
        }
    }

    private String getAccessToken(){
        RMapCache<String,String> corpVedaTokenCache = redissonClient.getMapCache(Constants.RedisKeys.CORP_VEDA_VENDOR_TOKEN);
        String value = corpVedaTokenCache.get(Constants.RedisKeys.CORP_VEDA_VENDOR_TOKEN);
        if(value == null){
            CorpVedaLoginResponseData loginResponse = getAndSaveLoginToken(corpVedaTokenCache);
            return loginResponse.getToken();
        }
        else {
            return value;
        }
    }

    private void StoreInCache(String token,RMapCache<String,String> cache){
        cache.put(Constants.RedisKeys.CORP_VEDA_VENDOR_TOKEN, token, 10, TimeUnit.SECONDS);
    }

    private CorpVedaLoginResponseData getAndSaveLoginToken(RMapCache<String,String> cache){
        CorpVedaLoginRequestData loginRequest = CorpVedaLoginRequestData.builder().username(appProperties.getCorpVedaUsername()).password(appProperties.getCorpVedaPassword()).build();
        CorpVedaLoginResponseData loginResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CORPVEDA,appProperties.getCorpVedaBaseUrl(),appProperties.getCorpVedaLoginUrl(), HttpMethod.POST,"",null,null,loginRequest,CorpVedaLoginResponseData.class);
        //Use RMapCache to store the token like in LOanApplicationService
        StoreInCache(loginResponse.getToken(),cache);
        return loginResponse;
    }

}
