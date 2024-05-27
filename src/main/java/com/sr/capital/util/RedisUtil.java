package com.sr.capital.util;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisUtil {

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(RedisUtil.class);
    final RedissonClient redissonClient;

    public String getValue(String key) {
        RBucket<String> cache = redissonClient.getBucket(key);
        return cache.get();
    }

    public boolean deleteKey(String key) {
        return redissonClient.getBucket(key).delete();
    }

    public long getTtlForKey(String key) {
        RBucket<String> fileCache = redissonClient.getBucket(key);
        return fileCache.remainTimeToLive();
    }
}
