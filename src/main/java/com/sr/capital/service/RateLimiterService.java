package com.sr.capital.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.helpers.constants.Constants;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RBucket;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RateLimiterService {
    private final RedissonClient redissonClient;
    private final AppProperties appProperties;

//    public boolean isAllowed(String clientId) {
//        String mapKey = Constants.RedisKeys.RATE_LIMIT_KEY_PREFIX + clientId;
//
//        // Use a consistent key within the map
//        String internalKey = "requestCount";
//
//        RMapCache<String, Long> mapCache = redissonClient.getMapCache(mapKey);
//
//        // Use atomic operations to ensure thread safety
//        Long value = mapCache.addAndGet(internalKey, 1L);
//
//        // If the value is 1, it means this is the first request, so set the expiration
//        if (value == 1L) {
//            mapCache.put(internalKey, 1L, appProperties.getRateLimitDurationInMinute(), TimeUnit.MINUTES);
//        }
//
//        // Check if the request is allowed based on the rate limit
//        return value <= appProperties.getRateLimitAllowedRequest();
//    }

    public boolean isAllowed(String clientId) {
        String mapKey = Constants.RedisKeys.RATE_LIMIT_KEY_PREFIX + clientId;

        String internalKey = Constants.RedisKeys.RATE_LIMIT_INTERNAL_KEY_PREFIX + clientId;

        RMapCache<String, Long> mapCache = redissonClient.getMapCache(mapKey);

        Long value = mapCache.addAndGet(internalKey, 1L);

        if (value == 1L) {
            mapCache.expire(Duration.ofMinutes(appProperties.getRateLimitDurationInMinute()));
        }

        return value <= appProperties.getRateLimitAllowedRequest();
    }
}