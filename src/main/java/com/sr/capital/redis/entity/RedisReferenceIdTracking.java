package com.sr.capital.redis.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "redis_event_tracking")
public class RedisReferenceIdTracking {

    private String redisKey;
    private Boolean isEventExecuted;
    private Boolean pushedIntoGetLoanDetailQueue;
    private Boolean pushedIntoStatusUpdateQueue;
    private int referenceId;
}
