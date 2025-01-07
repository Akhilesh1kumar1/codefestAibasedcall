package com.sr.capital.external.flexi.util;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.external.flexi.dto.response.LoanDetails;

import java.util.List;

public class FlexiLoanMapper {

    public static LoanStatusUpdateWebhookDto convertToWebhookDto(LoanDetails loanDetails) {
        LoanStatusUpdateWebhookDto webhookDto = new LoanStatusUpdateWebhookDto();

        // Map top-level fields
        webhookDto.setLoanCode(loanDetails.getData().getLoanCode());
        webhookDto.setLeadCode(loanDetails.getData().getLeadCode());
        webhookDto.setUpdatedAt(loanDetails.getData().getUpdatedAt());
        webhookDto.setCreatedAt(loanDetails.getData().getCreatedAt());
        webhookDto.setApplicationStatus(loanDetails.getData().getApplicationStatus());

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
//                        LoanStatusUpdateWebhookDto.Meta webhookMeta = new LoanStatusUpdateWebhookDto.Meta();
//                        webhookMeta.setSubCode(checkpoint.getMeta().getSubCode());
//                        webhookMeta.setCode(checkpoint.getMeta().getCode());
//                        webhookMeta.setFields(checkpoint.getMeta().getFields());
//                        webhookMeta.setMessage(checkpoint.getMeta().getMessage());
                        webhookCheckpoint.setMeta(checkpoint.getMeta());
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
