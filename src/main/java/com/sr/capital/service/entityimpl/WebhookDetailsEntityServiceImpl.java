package com.sr.capital.service.entityimpl;

import com.sr.capital.entity.mongo.WebhookDetails;
import com.sr.capital.entity.mongo.WebhookHistory;
import com.sr.capital.repository.mongo.WebhookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebhookDetailsEntityServiceImpl {

    final WebhookRepository webhookRepository;
    final WebhookHistoryServiceImpl webhookHistoryService;

    public WebhookDetails saveWebhookDetails(WebhookDetails webhookDetails){
        webhookRepository.save(webhookDetails);
        saveHistory(webhookDetails);
        return webhookDetails;
    }

    private void saveHistory(WebhookDetails webhookDetails) {
        WebhookHistory webhookHistory =WebhookHistory.builder().webhookDetails(webhookDetails).build();
        webhookHistoryService.saveWebhookHistory(webhookHistory);
    }
}
