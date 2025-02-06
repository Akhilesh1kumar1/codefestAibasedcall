package com.sr.capital.los.service;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.service.KafkaEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LOSExternalCallEventHandlerService implements KafkaEventService {

    final LosExternalCallHandlerServiceImpl losPanToGstExternalCallHandlerService;
    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        MsgMessage msgMessage = (MsgMessage) request;

        losPanToGstExternalCallHandlerService.handleExternalCall(msgMessage.getPayload());

        return (T) Boolean.TRUE;
    }
}
