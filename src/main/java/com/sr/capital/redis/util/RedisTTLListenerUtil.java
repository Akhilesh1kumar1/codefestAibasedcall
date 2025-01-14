package com.sr.capital.redis.util;

import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.KafkaEventTypes;
import com.sr.capital.redis.entity.RedisEventTracking;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;

import java.util.List;

import static com.sr.capital.CommonConstant.*;
import static com.sr.capital.helpers.enums.KafkaEventTypes.GET_LOAN_DETAILS;
import static com.sr.capital.helpers.enums.KafkaEventTypes.LOAN_STATUS_UPDATE;

public class RedisTTLListenerUtil {

    public static void updateStatus(String expiredKey, RedisEventTrackingRepo redisEventTrackingRepo, KafkaEventTypes kafkaEventTypes) {
        String key = generateKeyFromManipulatedString(expiredKey);
        RedisEventTracking redisEventTracking = redisEventTrackingRepo.findTopByRedisKey(key);
        if (redisEventTracking != null ) {
            redisEventTracking.setIsEventExecuted(true);
            if (kafkaEventTypes.name().equalsIgnoreCase(GET_LOAN_DETAILS.name())) {
                redisEventTracking.setPushedIntoGetLoanDetailQueue(true);
            } else if (kafkaEventTypes.name().equalsIgnoreCase(LOAN_STATUS_UPDATE.name())) {
                redisEventTracking.setPushedIntoStatusUpdateQueue(true);
            }
            redisEventTrackingRepo.save(redisEventTracking);
        }
    }

    public static String generateKeyFromManipulatedString(String manipulatedKey) {

        String[] parts = manipulatedKey.split("%%");

        if (parts.length < 4) {
            throw new IllegalArgumentException("Invalid key format: " + manipulatedKey);
        }

        // Extract components
        String loanAtVendor = parts[1];
        String internalLoanId = parts[2];
        String partner = parts[3];

        // Reconstruct the key
        return "%%" + loanAtVendor + "%%" + internalLoanId + "%%" + partner + "%%";
    }
}
