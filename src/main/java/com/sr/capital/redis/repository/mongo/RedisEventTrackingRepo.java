package com.sr.capital.redis.repository.mongo;

import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.redis.entity.RedisEventTracking;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RedisEventTrackingRepo extends MongoRepository<RedisEventTracking, String> {
    RedisEventTracking findByRedisKey(String redisKey);
    RedisEventTracking findTopByRedisKey(String redisKey);

}