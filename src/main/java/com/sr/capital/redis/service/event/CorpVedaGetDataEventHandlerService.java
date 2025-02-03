package com.sr.capital.redis.service.event;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.redis.service.impl.CorpVedaGetDataEventHandlerServiceImpl;
import com.sr.capital.service.KafkaEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorpVedaGetDataEventHandlerService implements KafkaEventService {

    final CorpVedaGetDataEventHandlerServiceImpl corpVedaGetDataEventHandlerServiceImpl;

    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        MsgMessage msgMessage = (MsgMessage) request;
        corpVedaGetDataEventHandlerServiceImpl.handleCorpVedaGetData(msgMessage.getPayload());
        return (T) Boolean.TRUE;
    }
}
