package com.sr.capital.redis.repository.mongo;

import com.sr.capital.redis.entity.RedisReferenceIdTracking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RedisReferenceIdTrackingRepository extends MongoRepository<RedisReferenceIdTracking,String> {
    RedisReferenceIdTracking findTopByRedisKey(String redisKey);
}
