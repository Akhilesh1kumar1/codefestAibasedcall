package com.sr.capital.service.impl;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.service.KafkaEventService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.constants.Constants.Headers.EVENT_TYPE;
import static com.sr.capital.helpers.constants.Constants.Headers.LOAN_VENDOR_NAME;

@Service
@RequiredArgsConstructor
public class StatusUpdateEventHandlerService implements KafkaEventService {

    final LoanStatusUpdateHandlerServiceImpl loanStatusUpdateHandlerService;
    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        MsgMessage msgMessage = (MsgMessage) request;

        LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = MapperUtils.readValue(msgMessage.getPayload(), LoanStatusUpdateWebhookDto.class);

        loanStatusUpdateHandlerService.handleStatusUpdate(loanStatusUpdateWebhookDto,msgMessage.getHeaders().get(LOAN_VENDOR_NAME));

        return (T) Boolean.TRUE;
    }
}
