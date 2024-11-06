package com.sr.capital.external.flexi.service;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.external.common.StatusMapperInterface;
import com.sr.capital.external.flexi.constants.Checkpoint;
import com.sr.capital.helpers.enums.LoanStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.sr.capital.external.flexi.constants.Checkpoint.PERSONAL_DETAILS;

@Service
public class FlexiStatusMapperServiceImpl implements StatusMapperInterface {
    @Override
    public LoanStatusUpdateWebhookDto mapStatus(Object loanApplicationDetails) {

        LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = (LoanStatusUpdateWebhookDto) loanApplicationDetails;

        switch (loanStatusUpdateWebhookDto.getS1().toLowerCase()){
            case "in progress":
                getInternalState(loanStatusUpdateWebhookDto);
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.APPLICATION_IN_PROGRESS.name());
                break;
            case "not approved":
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.REJECTED.name());
                break;
            case "approved"  :
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.APPROVED.name());

                break;
            case "disbursed":
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.DISBURSED.name());

                break;
        }

        return loanStatusUpdateWebhookDto;
    }

    private void getInternalState(LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto) {

        if(!CollectionUtils.isEmpty(loanStatusUpdateWebhookDto.getCheckpoints())){
            loanStatusUpdateWebhookDto.getCheckpoints().stream().forEach(checkpoint -> {

                switch (Checkpoint.valueOf(checkpoint.getCheckpoint())){
                    case PERSONAL_DETAILS:

                }

            });
        }
    }
}
