package com.sr.capital.external.corpveda.adapter;

import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.*;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import com.sr.capital.external.corpveda.repository.PlaceOrderRepository;
import com.sr.capital.external.corpveda.util.CorpVedaExternalRequestMetaData;
import com.sr.capital.external.corpveda.util.CorpVedaUtil;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.WebClientUtil;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
    private CorpVedaUtil corpVedaUtil;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private PlaceOrderRepository placeOrderRepository;

    private final com.sr.capital.util.LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(CorpVedaAdapter.class);

    public <T extends CorpVedaBaseRequest<?>, U extends CorpVedaBaseResponse<?>> U extractDetails(final CorpVedaDocOrChestratorRequest request) throws Exception{
        LoggerUtil.info("CorpVedaAdapter.extractDetails() started");
        CorpVedaExternalRequestMetaData metaData = getRequestEndPointAndDocType(request);
        try {
            String getRedisToken = getAccessToken();
            HttpHeaders headers = corpVedaUtil.setHeaders(getRedisToken);
            try{
                 return (U) webClientUtil.makeExternalCallBlocking(ServiceName.CORPVEDA,
                        appProperties.getCorpVedaBaseUrl(),
                         metaData.getEndpoint(),
                        HttpMethod.POST,
                        "",
                        headers,
                        null,
                        MapperUtils.convertToString(request.getCorpVedaBaseRequest().getDetails()),
                        metaData.getResponseClass());
            }
            //3rd API integration
            //ScheduleGetDataApiHook(placeOrderServiceResponseData.getReferenceId());
            catch (Exception e){
                LoggerUtil.error(e.getMessage());
                throw new Exception(metaData.getDocType().getName() + "--" +e.getMessage() + " ");
            }
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
        cache.put(Constants.RedisKeys.CORP_VEDA_VENDOR_TOKEN, token, Long.parseLong(appProperties.getCorpVedaTokenTime()), TimeUnit.HOURS);
    }

    private CorpVedaLoginResponseData getAndSaveLoginToken(RMapCache<String,String> cache){
        CorpVedaLoginRequestData loginRequest = CorpVedaLoginRequestData.builder().username(appProperties.getCorpVedaUsername()).password(appProperties.getCorpVedaPassword()).build();
        try {
            CorpVedaLoginResponseData loginResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CORPVEDA, appProperties.getCorpVedaBaseUrl(), appProperties.getCorpVedaLoginUrl(), HttpMethod.POST, "", null, null, loginRequest, CorpVedaLoginResponseData.class);
            StoreInCache(loginResponse.getToken(),cache);
            return loginResponse;
        }
        catch (Exception e){
            new Exception("Error while getting token from CorpVeda");
        }
        return null;
    }

    public void ScheduleGetDataApiHook(int referenceId){
        String key = getKeyForCache(String.valueOf(referenceId));
        redissonClient.getMapCache(key).put(key, true, 30, TimeUnit.MINUTES);
    }

    private String getKeyForCache(String referenceId) {
        return "%%" + Constants.RedisKeys.CORP_VEDA_META_DATA + "%%" + referenceId + "%%";
    }

    private <T extends CorpVedaBaseRequest<?>> CorpVedaExternalRequestMetaData getRequestEndPointAndDocType(final CorpVedaDocOrChestratorRequest request) throws ServiceEndpointNotFoundException {
        if (request.getCorpVedaBaseRequest().getDetails() instanceof CorpVedaPlaceOrderRequestExtractionData){
            return CorpVedaExternalRequestMetaData.builder()
                    .endpoint(appProperties.getCorpVedaPlaceOrderUrl())
                    .docType(CorpVedaDocType.CIN_SEARCH_PLACE_ORDER)
                    .responseClass(PlaceOrderServiceResponseData.class)
                    .build();
        } else if (request.getCorpVedaBaseRequest().getDetails() instanceof CorpVedaGetOrderRequestExtractionData) {
            return CorpVedaExternalRequestMetaData.builder()
                    .endpoint(appProperties.getCorpVedaGetDataUrl())
                    .docType(CorpVedaDocType.CIN_SEARCH_GET_DATA)
                    .responseClass(PlaceOrderServiceResponseData.class)
                    .build();
        } else {
            throw new ServiceEndpointNotFoundException();
        }

    }
}
