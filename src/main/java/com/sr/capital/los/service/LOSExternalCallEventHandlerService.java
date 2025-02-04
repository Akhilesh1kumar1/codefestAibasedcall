package com.sr.capital.los.service;

import com.omunify.kafka.MsgMessage;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.service.KafkaEventService;
import com.sr.capital.util.MapperUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sr.capital.helpers.constants.Constants.Headers.LOAN_VENDOR_NAME;

@Service
@RequiredArgsConstructor
public class LOSExternalCallEventHandlerService implements KafkaEventService {

    final LosPanToGstExternalCallHandlerServiceImpl losPanToGstExternalCallHandlerService;
    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        MsgMessage msgMessage = (MsgMessage) request;

        losPanToGstExternalCallHandlerService.handleLOSExternalCall(msgMessage.getPayload());

        return (T) Boolean.TRUE;
    }
}
