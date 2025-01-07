package com.sr.capital.redis.service.event;
import com.sr.capital.config.AppProperties;
import com.sr.capital.config.RedisConfig;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;
import com.sr.capital.redis.util.RedisTTLListenerUtil;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RPatternTopic;
import org.redisson.api.RedissonClient;
import org.redisson.api.listener.MessageListener;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.enums.KafkaEventTypes.GET_LOAN_DETAILS;

@Component
@Slf4j
public class RedisExpiryListener {
    private final RedissonClient redissonClient;

    private final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;
    private final AppProperties appProperties;
    private final RedisEventTrackingRepo redisEventTrackingRepo;


    public RedisExpiryListener(RedissonClient redissonClient, KafkaMessagePublisherUtil kafkaMessagePublisherUtil, AppProperties appProperties, RedisEventTrackingRepo redisEventTrackingRepo) {
        this.redissonClient = redissonClient;
        this.kafkaMessagePublisherUtil = kafkaMessagePublisherUtil;
        this.appProperties = appProperties;
        this.redisEventTrackingRepo = redisEventTrackingRepo;
    }

    @PostConstruct
    public void init() {
        setupExpirationListener();
    }

    private void setupExpirationListener() {
        String expireChannel = "__keyevent@0__:expired"; // Channel for key expiration events

        redissonClient.getTopic(expireChannel, new StringCodec()).addListener(String.class, new MessageListener<String>() {
            @Override
            public void onMessage(CharSequence channel, String msg) {

            log.info("TTL listener is executing for key ::" + msg);

            if (msg.contains(Constants.RedisKeys.LOAN_AT_VENDOR)) {
                handleKeyExpiration(msg);
            }

            }
        });
    }
    private void handleKeyExpiration(String expiredKey) {
        RedisTTLListenerUtil.updateStatus(expiredKey, redisEventTrackingRepo, GET_LOAN_DETAILS);
        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.
                getKafkaMessage(expiredKey, GET_LOAN_DETAILS.name(),null,null, null));
    }

}