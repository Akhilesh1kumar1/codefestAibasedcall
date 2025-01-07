package com.sr.capital.redis.service.event;
import com.sr.capital.config.AppProperties;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;
import com.sr.capital.redis.util.RedisTTLListenerUtil;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.enums.KafkaEventTypes.GET_LOAN_DETAILS;

/*@Service
@Slf4j
@RequiredArgsConstructor*/
public class RedisExpiryListener {
    /*
    private final RedissonClient redissonClient;

    private final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;
    private final AppProperties appProperties;
    private final RedisEventTrackingRepo redisEventTrackingRepo;

        @PostConstruct
    public void listenToExpiryEvents() {
        // Subscribe to the key event notification channel for expired keys
        String expiryChannel = "__keyevent@0__:expired"; // Replace "0" with your Redis DB index if different
        RPatternTopic topic = redissonClient.getPatternTopic(expiryChannel);
            log.info("TTL listener is executing for key :: 1");

            topic.addListener(String.class, ((pattern, channel, message) -> {

            log.info("TTL listener is executing for key ::" + message);

            if (message.contains(Constants.RedisKeys.LOAN_AT_VENDOR)) {
                handleKeyExpiration(message);
            }
        }));
            log.info("TTL listener is executing for key :: 2");

        }
    private void handleKeyExpiration(String expiredKey) {
        RedisTTLListenerUtil.updateStatus(expiredKey, redisEventTrackingRepo, GET_LOAN_DETAILS);
        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.
                getKafkaMessage(expiredKey, GET_LOAN_DETAILS.name(),null,null, null));
    }*/

}