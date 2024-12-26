package com.sr.capital.redis.entity;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Document(collection = "redis_event_tracking")
public class RedisEventTracking extends BaseDoc {

    private String redisKey;
    private Boolean isEventExecuted;
    private Boolean pushedIntoGetLoanDetailQueue;
    private Boolean pushedIntoStatusUpdateQueue;
    private String internalLoanId;
    private String partner;
}