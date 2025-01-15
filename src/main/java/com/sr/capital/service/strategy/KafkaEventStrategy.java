package com.sr.capital.service.strategy;

import com.sr.capital.excelprocessor.event.ProcessUploadedDataEventHandlerService;
import com.sr.capital.exception.custom.RequestValidatorNotFoundException;
import com.sr.capital.helpers.enums.KafkaEventTypes;
import com.sr.capital.redis.service.event.GetLoanDetailsEventHandlerService;
import com.sr.capital.service.KafkaEventService;
import com.sr.capital.service.impl.StatusUpdateEventHandlerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class KafkaEventStrategy {

    final StatusUpdateEventHandlerService statusUpdateEventHandlerService;
    final GetLoanDetailsEventHandlerService getLoanDetailsEventHandlerService;
    final ProcessUploadedDataEventHandlerService processUploadedDataEventHandlerService;


    public <T,U> T handleEvents(U request, KafkaEventTypes type) throws Exception {
        KafkaEventService kafkaEventService;
        switch (type) {
            case LOAN_STATUS_UPDATE -> kafkaEventService = statusUpdateEventHandlerService;
            case GET_LOAN_DETAILS -> kafkaEventService = getLoanDetailsEventHandlerService;
            case PROCESS_UPLOAD_DATA_EVENT -> kafkaEventService = processUploadedDataEventHandlerService;
            default -> {
                throw new RequestValidatorNotFoundException();
            }
        }
        return kafkaEventService.handleEvents(request);
    }
}
