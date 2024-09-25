package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.WebhookHistory;
import com.sr.capital.repository.mongo.WebhookHistoryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookHistoryServiceImpl {

    final WebhookHistoryRepository webhookHistoryRepository;


    public void saveWebhookHistory(WebhookHistory webhookHistory){
        webhookHistoryRepository.save(webhookHistory);
    }
}
