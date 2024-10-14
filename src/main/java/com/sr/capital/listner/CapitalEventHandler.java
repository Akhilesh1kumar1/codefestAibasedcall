package com.sr.capital.listner;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.sr.capital.helpers.constants.Constants.Headers.EVENT_TYPE;
import static com.sr.capital.helpers.constants.Constants.Headers.MESSAGE_HEADER;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CapitalEventHandler{ //implements MessageHandler {

    /*final KafkaEventStrategy kafkaEventStrategy;


    @Override
    public void handle(MsgMessage message) throws Exception {
        setRequestMeta(message);

        handleEvents(message);

      }

    private void handleEvents(MsgMessage message) throws Exception {

        kafkaEventStrategy.handleEvents(message, KafkaEventTypes.valueOf(message.getHeaders().get(EVENT_TYPE)));

    }

    @Override
    public String getHandlerName() {
        return "kafkaEvents";
    }

    private void setRequestMeta(MsgMessage message) throws CustomException {
        RequestData.setTenantId(TenantUtils.fetchTenantId(message));
        RequestData.setMessageId(StringUtils.isNotEmpty(message.getHeaders().get(MESSAGE_HEADER)) ? (message.getHeaders().get(MESSAGE_HEADER)): UUID.randomUUID().toString());
    }*/
}
