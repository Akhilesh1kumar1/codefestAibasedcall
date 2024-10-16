package com.sr.capital.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omunify.kafka.MsgMessage;
import com.omunify.kafka.publisher.MessagePublisher;
import com.omunify.kafka.publisher.exception.FailedToPublishMessageException;
import com.omunify.kafka.publisher.exception.FailedToSaveUnPublishedMessageException;
import com.sr.capital.dto.RequestData;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

import static com.sr.capital.helpers.constants.Constants.Headers.*;

@Component
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KafkaMessagePublisherUtil {

    final MessagePublisher messagePublisher;
    final ObjectMapper objectMapper;


    public void publishMessage(String topicName, MsgMessage message){
        try {
            //TODO temporary logs which needs to be removed later
            log.info("Publishing to topic {} with messageId {} message {}",topicName,message.getMessageId(), objectMapper.writeValueAsString(message));
            messagePublisher.publishMessage(topicName, message);
        } catch (FailedToPublishMessageException | FailedToSaveUnPublishedMessageException | JsonProcessingException exception) {
            log.error("Message publish failed for message with id {} and error {}", message.getMessageId(), exception.getMessage());
        }
    }

    public MsgMessage getKafkaMessage(String payload, String eventType, String groupId, String correlationId,String loanVendorName) {
        MsgMessage msgMessage = new MsgMessage();
        msgMessage.setPayload(payload);
        msgMessage.setEventType(eventType);
        if(!StringUtils.isEmpty(groupId)){
            msgMessage.setGroupId(groupId);
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(CORRELATION_HEADER, correlationId);
        headers.put(TENANT_HEADER, RequestData.getTenantId());
        headers.put(LOAN_VENDOR_NAME,loanVendorName);
        msgMessage.setHeaders(headers);
        return msgMessage;
    }

}