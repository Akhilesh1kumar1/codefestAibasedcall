package com.sr.capital.redis.service.impl;

import com.sr.capital.CommonConstant;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.entity.primary.LoanApplication;
import com.sr.capital.external.flexi.dto.response.LoanDetails;
import com.sr.capital.external.flexi.util.FlexiLoanMapper;
import com.sr.capital.redis.repository.mongo.RedisEventTrackingRepo;
import com.sr.capital.redis.util.RedisTTLListenerUtil;
import com.sr.capital.repository.primary.LoanApplicationRepository;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.enums.KafkaEventTypes.GET_LOAN_DETAILS;
import static com.sr.capital.helpers.enums.KafkaEventTypes.LOAN_STATUS_UPDATE;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetLoanDetailsEventHandlerServiceImpl {

    private final RedissonClient redissonClient;
    private final LoanApplicationRepository loanApplicationRepository;
    private final CreditPartnerFactoryService creditPartnerFactoryService;
    final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;
    final AppProperties appProperties;
    final RedisEventTrackingRepo redisEventTrackingRepo;

    public void handleGetLoanDetails(String expiredKey) {
        try {
            String internalLoanId = expiredKey.split("%%")[2]; //getDataFromString(expiredKey, CommonConstant.INTERNAL_LOAN_ID);
            String partner = expiredKey.split("%%")[3];
            if (internalLoanId != null && !internalLoanId.isEmpty()) {
                LoanApplication loanApplication = loanApplicationRepository.findByInternalLoanId(internalLoanId);
                LoanMetaDataDto loanMetaDataDto = LoanMetaDataDto.builder().loanVendorId(loanApplication.getLoanVendorId()).loanVendorName(partner).loanId(loanApplication.getVendorLoanId()).build();

                Object object = creditPartnerFactoryService.getPartnerService(partner).getLoanDetails(loanMetaDataDto);
                LoanDetails loanDetails = MapperUtils.convertValue(object, LoanDetails.class);
                LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = FlexiLoanMapper.convertToWebhookDto(loanDetails);
                RedisTTLListenerUtil.updateStatus(expiredKey, redisEventTrackingRepo, LOAN_STATUS_UPDATE);
                kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(),kafkaMessagePublisherUtil.getKafkaMessage(MapperUtils.writeValueAsString(loanStatusUpdateWebhookDto), LOAN_STATUS_UPDATE.name(),null,null,partner));
            }

        } catch (Exception e) {
            log.error("Error while pushing status change queue : " + e.getMessage() + " error is " + e);
        }
    }
}
