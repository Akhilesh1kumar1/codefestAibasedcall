package com.sr.capital.external.flexi.util;

import com.mashape.unirest.http.ObjectMapper;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.external.flexi.dto.response.LoanDetails;
import com.sr.capital.util.MapperUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j
public class FlexiLoanMapper {

    public static LoanStatusUpdateWebhookDto convertToWebhookDto(LoanDetails loanDetails) {
        LoanStatusUpdateWebhookDto webhookDto = new LoanStatusUpdateWebhookDto();

        // Map top-level fields
        webhookDto.setLoanCode(loanDetails.getData().getLoanCode());
        webhookDto.setLeadCode(loanDetails.getData().getLeadCode());
        webhookDto.setUpdatedAt(loanDetails.getData().getUpdatedAt());
        webhookDto.setCreatedAt(loanDetails.getData().getCreatedAt());
        webhookDto.setApplicationStatus(loanDetails.getData().getApplicationStatus());
        webhookDto.setS1(loanDetails.getData().getS1());
        webhookDto.setS2(loanDetails.getData().getS2());
        webhookDto.setS3(loanDetails.getData().getS3());

        // Map checkpoints if present
        if (loanDetails.getData().getCheckpoints() != null) {
            List<LoanStatusUpdateWebhookDto.Checkpoint> webhookCheckpoints = loanDetails.getData().getCheckpoints()
                .stream()
                .map(checkpoint -> {
                    LoanStatusUpdateWebhookDto.Checkpoint webhookCheckpoint = new LoanStatusUpdateWebhookDto.Checkpoint();
                    webhookCheckpoint.setCheckpoint(checkpoint.getCheckpoint());
                    webhookCheckpoint.setState(checkpoint.getState());

                    // Map Meta if present
                    if (checkpoint.getMeta() != null) {
                        try {
                            webhookCheckpoint.setMeta(MapperUtils.convertValue(checkpoint.getMeta(), LoanStatusUpdateWebhookDto.Meta.class));
                        } catch (IOException e) {
                            log.error("Error while converting file" + e);
                        }
                    }

                    return webhookCheckpoint;
                })
                .toList();
            webhookDto.setCheckpoints(webhookCheckpoints);
        }

        // Return the mapped DTO
        return webhookDto;
    }
}
