package com.sr.capital.listner;

import com.sr.capital.config.AppProperties;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;
import com.sr.capital.redis.util.RedisTTLListenerUtil;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import com.sr.capital.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import static com.sr.capital.helpers.enums.KafkaEventTypes.CORP_VEDA_GET_DATA;
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
    final WebClientUtil webClientUtil;


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String expiredKey = message.toString();

        log.info("TTL listener is executing for key ::" + expiredKey);
        if (expiredKey.contains(Constants.RedisKeys.LOAN_AT_VENDOR)) {
            handleLoanAtVendorKeyExpiration(expiredKey);
        } else if (expiredKey.contains(Constants.RedisKeys.CORP_VEDA_META_DATA)) {
            handleVerboseDataKeyExpiration(expiredKey);
        }
    }

    private void handleLoanAtVendorKeyExpiration(String expiredKey) {
        RedisTTLListenerUtil.updateStatus(expiredKey, redisEventTrackingRepo, GET_LOAN_DETAILS);
        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.
                getKafkaMessage(expiredKey, GET_LOAN_DETAILS.name(),null,null, null));
    }

    private void handleVerboseDataKeyExpiration(String expiredKey) {
        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.
                getKafkaMessage(expiredKey, CORP_VEDA_GET_DATA.name(),null,null, null));
    }
}
