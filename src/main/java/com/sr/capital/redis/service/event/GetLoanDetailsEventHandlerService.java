package com.sr.capital.redis.service.event;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.redis.service.impl.GetLoanDetailsEventHandlerServiceImpl;
import com.sr.capital.service.KafkaEventService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.constants.Constants.Headers.EVENT_TYPE;
import static com.sr.capital.helpers.constants.Constants.Headers.LOAN_VENDOR_NAME;

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
