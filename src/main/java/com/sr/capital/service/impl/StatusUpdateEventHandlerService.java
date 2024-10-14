package com.sr.capital.service.impl;

import com.sr.capital.service.KafkaEventService;
import org.springframework.stereotype.Service;

@Service
public class StatusUpdateEventHandlerService implements KafkaEventService {

    @Override
    public <T, U> T handleEvents(U request) throws Exception {
        return null;
    }
}
