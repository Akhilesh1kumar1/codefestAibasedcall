package com.sr.capital.external.flexi.service;

import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.external.common.StatusMapperInterface;
import com.sr.capital.external.flexi.constants.Checkpoint;
import com.sr.capital.helpers.enums.LoanStatus;
import com.sr.capital.helpers.enums.Screens;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class FlexiStatusMapperServiceImpl implements StatusMapperInterface {
    @Override
    public LoanStatusUpdateWebhookDto mapStatus(Object loanApplicationDetails) {

        LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = (LoanStatusUpdateWebhookDto) loanApplicationDetails;

        loanStatusUpdateWebhookDto.setStatus(loanStatusUpdateWebhookDto.getS1());
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
                loanStatusUpdateWebhookDto.setInternalStatus(LoanStatus.LOAN_DISBURSED.name());
                loanStatusUpdateWebhookDto.setInternalState(LoanStatus.LOAN_DISBURSED.name());
                break;
            default:
                // Optionally handle unknown status here
                break;
        }
        return loanStatusUpdateWebhookDto;
    }

    private void handleInProgressStatus(LoanStatusUpdateWebhookDto dto) {
        if (!CollectionUtils.isEmpty(dto.getCheckpoints())) {
            for (LoanStatusUpdateWebhookDto.Checkpoint checkpoint : dto.getCheckpoints()) {
                if(Checkpoint.isValuePresent(checkpoint.getCheckpoint())) {
                    Boolean currentStateFound = processCheckpoint(dto, checkpoint);
                    if (currentStateFound)
                        return;
                }
            }
        }
    }

    private void handleNotApprovedStatus(LoanStatusUpdateWebhookDto dto) {
        String currentStatus = dto.getInternalCurrentStatus();
        if (LoanStatus.LEAD_DOCUMENT_UPLOAD.name().equalsIgnoreCase(currentStatus) ||
                LoanStatus.LEAD_IN_PROGRESS.name().equalsIgnoreCase(currentStatus) ||  LoanStatus.LEAD_REJECTED.name().equalsIgnoreCase(currentStatus)) {
            dto.setInternalStatus(LoanStatus.LEAD_REJECTED.name());
        } else if(LoanStatus.LOAN_GENERATE.name().equalsIgnoreCase(currentStatus) || LoanStatus.LOAN_DECLINE.name().equalsIgnoreCase(currentStatus)){
            dto.setInternalStatus(LoanStatus.LOAN_DECLINE.name());
        }else{
            dto.setInternalStatus(LoanStatus.LEAD_DECLINE.name());

        }
    }

    private void handleApprovedStatus(LoanStatusUpdateWebhookDto dto) {
        if(LoanStatus.LEAD_PROCESSING.name().equalsIgnoreCase(dto.getInternalCurrentStatus())){
            dto.setInternalStatus(LoanStatus.LOAN_GENERATE.name());
        }
       /* else if (LoanStatus.LOAN_VERIFICATION.name().equalsIgnoreCase(dto.getInternalCurrentStatus())) {
            dto.setInternalStatus(LoanStatus.LOAN_ACCEPTED.name());
        } else {
            dto.setInternalStatus(LoanStatus.LOAN_VERIFICATION.name());
        }*/
    }

    private Boolean processCheckpoint(LoanStatusUpdateWebhookDto dto, LoanStatusUpdateWebhookDto.Checkpoint checkpoint) {
        String state = checkpoint.getState();
        Boolean currentStateFound =false;
        switch (Checkpoint.valueOf(checkpoint.getCheckpoint())) {
            case PERSONAL_DETAILS:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    Map<String,String> tempFieldsMap =new HashMap<>();
                    buildPerosnalDetailsFieldMap(tempFieldsMap);
                    setInternalState(dto, Screens.PERSONAL_DETAILS, LoanStatus.UPDATE_LEAD_IN_PROGRESS);
                    if(checkpoint.getMeta()!=null && !CollectionUtils.isEmpty(checkpoint.getMeta().getFields())){
                        List<String> fields = new ArrayList<>();
                        checkpoint.getMeta().getFields().forEach(k->{
                            fields.add(tempFieldsMap.getOrDefault(k.toLowerCase(),k));
                        });
                        checkpoint.getMeta().setFields(fields);
                    }
                    currentStateFound =true;
                }

                break;
            case BUSINESS_DETAILS:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    Map<String,String> tempFieldsMap =new HashMap<>();
                    buildBusinessDetailsFieldMap(tempFieldsMap);
                    setInternalState(dto, Screens.BUSINESS_DETAILS, LoanStatus.UPDATE_LEAD_IN_PROGRESS);
                    if(checkpoint.getMeta()!=null && !CollectionUtils.isEmpty(checkpoint.getMeta().getFields())){
                        List<String> fields = new ArrayList<>();
                        checkpoint.getMeta().getFields().forEach(k->{
                            fields.add(tempFieldsMap.getOrDefault(k.toLowerCase(),k));
                        });
                        checkpoint.getMeta().setFields(fields);
                    }
                    currentStateFound =true;
                }
                break;
            case DOCUMENTS_RECEIVED:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.PENDING_DOCUMENT, LoanStatus.LEAD_DOCUMENT_UPLOAD);
                    currentStateFound =true;
                }
                break;
            case DOCUMENTS_VERIFIED:
                if ("ERRORED".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.PENDING_DOCUMENT, LoanStatus.LEAD_DOCUMENT_UPLOAD);
                    currentStateFound =true;
                } else  if ("PENDING".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.DOCUMENT_VERIFICATION, LoanStatus.LEAD_PROCESSING);
                    currentStateFound =true;
                }
                break;
            case LOAN_APPROVED:
                if ("SUCCESS".equalsIgnoreCase(state)) {
                    setInternalState(dto, Screens.LOAN_SANCTION, LoanStatus.LOAN_GENERATE);
                    currentStateFound =true;
                } else {
                    setInternalState(dto, Screens.DOCUMENT_VERIFICATION, LoanStatus.LEAD_PROCESSING);
                    currentStateFound =true;
                }
                break;
            default:
                break;
        }
        return  currentStateFound;
    }

    private void buildPerosnalDetailsFieldMap(Map<String, String> tempFieldsMap) {

        tempFieldsMap.put("address_building","address");
        tempFieldsMap.put("address_area","address");
        tempFieldsMap.put("pan_no","pan_number");
        tempFieldsMap.put("name","name");
        tempFieldsMap.put("address_pincode","pincode");

    }

    private void buildBusinessDetailsFieldMap(Map<String, String> tempFieldsMap) {

        tempFieldsMap.put("address_building","businessAddress");
        tempFieldsMap.put("address_area","businessAddress");
        tempFieldsMap.put("address_pin_code","companyPincode");

    }

    private void setInternalState(LoanStatusUpdateWebhookDto dto, Screens screen, LoanStatus status) {
        dto.setInternalState(screen.name());
        dto.setInternalStatus(status.name());
    }

}
