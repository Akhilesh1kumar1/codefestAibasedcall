package com.sr.capital.external.flexi.service;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.external.common.StatusMapperInterface;
import com.sr.capital.external.flexi.constants.Checkpoint;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.Screens;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


@Service
public class FlexiStatusMapperServiceImpl implements StatusMapperInterface {
    @Override
    public LoanStatusUpdateWebhookDto mapStatus(Object loanApplicationDetails) {

        LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = (LoanStatusUpdateWebhookDto) loanApplicationDetails;

        switch (loanStatusUpdateWebhookDto.getS1().toLowerCase()) {
            case "in progress":
                handleInProgressStatus(loanStatusUpdateWebhookDto);
                break;
            case "not approved":
                handleNotApprovedStatus(loanStatusUpdateWebhookDto);
                break;
            case "approved":
                handleApprovedStatus(loanStatusUpdateWebhookDto);
                break;
            case "disbursed":
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.DISBURSED.name());
                loanStatusUpdateWebhookDto.setInternalState(LoanStatus.DISBURSED.name());
                break;
            default:
                // Optionally handle unknown status here
                break;
        }
        return loanStatusUpdateWebhookDto;
    }

    private void handleInProgressStatus(LoanStatusUpdateWebhookDto dto) {
        if (!CollectionUtils.isEmpty(dto.getCheckpoints())) {
            dto.getCheckpoints().forEach(checkpoint -> processCheckpoint(dto, checkpoint));
        }
    }

    private void handleNotApprovedStatus(LoanStatusUpdateWebhookDto dto) {
        String currentStatus = dto.getInternalCurrentStatus();
        if (LoanStatus.LEAD_DOCUMENT_UPLOAD.name().equalsIgnoreCase(currentStatus) ||
                LoanStatus.LEAD_PROCESSING.name().equalsIgnoreCase(currentStatus)) {
            dto.setInternalStatus(LoanStatus.LEAD_DECLINE.name());
        } else {
            dto.setInternalStatus(LoanStatus.LEAD_REJECTED.name());
        }
    }

    private void handleApprovedStatus(LoanStatusUpdateWebhookDto dto) {
        if (LoanStatus.LOAN_VERIFICATION.name().equalsIgnoreCase(dto.getInternalCurrentStatus())) {
            dto.setInternalStatus(LoanStatus.LOAN_ACCEPTED.name());
        } else {
            dto.setInternalStatus(LoanStatus.LOAN_VERIFICATION.name());
        }
    }

    private void processCheckpoint(LoanStatusUpdateWebhookDto dto, LoanStatusUpdateWebhookDto.Checkpoint checkpoint) {
        String state = checkpoint.getState();
        switch (Checkpoint.valueOf(checkpoint.getCheckpoint())) {
            case PERSONAL_DETAILS:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.PERSONAL_DETAILS, LoanStatus.LEAD_IN_PROGRESS);
                }
                break;
            case BUSINESS_DETAILS:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.BUSINESS_DETAILS, LoanStatus.LEAD_IN_PROGRESS);
                }
                break;
            case DOCUMENTS_RECEIVED:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.PENDING_DOCUMENT, LoanStatus.LEAD_DOCUMENT_UPLOAD);
                }
                break;
            case DOCUMENTS_VERIFIED:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.PENDING_DOCUMENT, LoanStatus.LEAD_DOCUMENT_UPLOAD);
                } else {
                    setInternalState(dto, Screens.DOCUMENT_VERIFICATION, LoanStatus.LEAD_PROCESSING);
                }
                break;
            case LOAN_APPROVED:
                if ("SUCCESS".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.LOAN_SANCTION, LoanStatus.LOAN_GENERATE);
                } else {
                    setInternalState(dto, Screens.DOCUMENT_VERIFICATION, LoanStatus.LEAD_PROCESSING);
                }
                break;
            default:
                break;
        }
    }

    private void setInternalState(LoanStatusUpdateWebhookDto dto, Screens screen, LoanStatus status) {
        dto.setInternalState(screen.name());
        dto.setInternalStatus(status.name());
    }

}
