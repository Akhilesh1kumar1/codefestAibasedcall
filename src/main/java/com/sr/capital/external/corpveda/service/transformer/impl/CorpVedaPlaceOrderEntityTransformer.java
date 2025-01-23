package com.sr.capital.external.corpveda.service.transformer.impl;


import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.data.PlaceOrderServiceResponseData;
import com.sr.capital.external.corpveda.entity.PartnerFreeDataDetails;
import com.sr.capital.external.corpveda.repository.PlaceOrderRepository;
import com.sr.capital.external.corpveda.service.transformer.CorpVedaEntityTransformer;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.redis.entity.RedisReferenceIdTracking;
import com.sr.capital.redis.repository.mongo.RedisReferenceIdTrackingRepository;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CorpVedaPlaceOrderEntityTransformer implements CorpVedaEntityTransformer {

    private final PlaceOrderRepository placeOrderRepository;

    private final RedissonClient redissonClient;

    private final RedisReferenceIdTrackingRepository redisReferenceIdTrackingRepository;

    private final AppProperties appProperties;

    @Override
    public <T> T constructEntity(CorpVedaDocOrChestratorRequest request, T entity) throws IOException {
        PlaceOrderServiceResponseData placeOrderServiceResponseData = MapperUtils.convertValue(request.getCOrpVedaBaseResponse(), PlaceOrderServiceResponseData.class);
        PartnerFreeDataDetails placeOrderServiceDetails = MapperUtils.convertValue(placeOrderServiceResponseData, PartnerFreeDataDetails.class);
        placeOrderServiceDetails.setSrCompanyId(request.getSrCompanyId());
        placeOrderRepository.save(placeOrderServiceDetails);
        ScheduleGetDataApiHook(placeOrderServiceDetails.getReferenceId());
        return (T) CorpVedaDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .details(placeOrderServiceDetails)
                .corpVedaDocType(request.getDocType())
                .build();
    }


    public void ScheduleGetDataApiHook(int referenceId){
        String key = getKeyForCache(String.valueOf(referenceId));
        redissonClient.getMapCache(key).put(key, true, Long.parseLong(appProperties.getCorpVedaRedisReferenceIdTime()), TimeUnit.MINUTES);
        SaveInDB(referenceId,key);
    }

    private String getKeyForCache(String referenceId) {
        return "%%" + Constants.RedisKeys.CORP_VEDA_META_DATA + "%%" + referenceId + "%%";
    }

    private void SaveInDB(int referenceId, String key) {
        RedisReferenceIdTracking redisReferenceIdTracking = RedisReferenceIdTracking.builder()
                .redisKey(key)
                .isEventExecuted(false)
                .referenceId(referenceId)
                .build();
        redisReferenceIdTrackingRepository.save(redisReferenceIdTracking);
    }




}
