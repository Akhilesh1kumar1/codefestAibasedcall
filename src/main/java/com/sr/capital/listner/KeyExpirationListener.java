package com.sr.capital.listner;

import com.sr.capital.CommonConstant;
import com.sr.capital.config.AppProperties;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.KafkaEventTypes;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;
import com.sr.capital.redis.util.RedisTTLListenerUtil;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import static com.sr.capital.helpers.enums.KafkaEventTypes.GET_LOAN_DETAILS;


@Slf4j
@Component
@RequiredArgsConstructor
public class KeyExpirationListener implements MessageListener {

    private final RedissonClient redissonClient;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CreditPartnerFactoryService creditPartnerFactoryService;
    final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;
    final AppProperties appProperties;
    final RedisEventTrackingRepo redisEventTrackingRepo;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        log.info("TTL listener is executing for key ::" + expiredKey);
        if (expiredKey.contains(Constants.RedisKeys.LOAN_AT_VENDOR)) {
            handleKeyExpiration(expiredKey);
        }
    }

    private void handleKeyExpiration(String expiredKey) {
        RedisTTLListenerUtil.updateStatus(expiredKey, redisEventTrackingRepo, GET_LOAN_DETAILS);
        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.
                getKafkaMessage(expiredKey, GET_LOAN_DETAILS.name(),null,null, null));
    }
}
