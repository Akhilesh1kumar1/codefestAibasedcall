package com.sr.capital.redis.service.event;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.redis.service.impl.GetLoanDetailsEventHandlerServiceImpl;
import com.sr.capital.service.KafkaEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetLoanDetailsEventHandlerService implements KafkaEventService {

    final GetLoanDetailsEventHandlerServiceImpl getLoanDetailsEventHandlerServiceImpl;
    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        MsgMessage msgMessage = (MsgMessage) request;

        getLoanDetailsEventHandlerServiceImpl.handleGetLoanDetails(msgMessage.getPayload());

        return (T) Boolean.TRUE;
    }
}
