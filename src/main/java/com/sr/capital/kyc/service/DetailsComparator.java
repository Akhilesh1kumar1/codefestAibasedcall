package com.sr.capital.kyc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sr.capital.config.AppProperties;

import com.sr.capital.entity.primary.Task;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.exception.custom.IncompatibleDetailsException;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.exception.custom.TaskProcessingException;
import com.sr.capital.exception.custom.VerifierNotFound;
import com.sr.capital.external.client.SinfiniClient;
import com.sr.capital.helpers.constants.ErrorConstants;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.TaskStatus;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.helpers.enums.VerificationStatus;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.ErrorMessageRequest;
import com.sr.capital.kyc.dto.response.VerifierResponse;
import com.sr.capital.kyc.external.exception.IDfyNameComparisonException;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.strategy.VerifierStrategy;
import com.sr.capital.service.entityimpl.TaskManager;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.util.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DetailsComparator {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private VerifierStrategy verifierStrategy;

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(DetailsComparator.class);


    @Transactional
    public void verifyDetailsAndUpdateTasks(DocOrchestratorRequest docOrchestratorRequest)
        throws VerifierNotFound, IncompatibleDetailsException, JsonProcessingException,
            IDfyNameComparisonException, ServiceEndpointNotFoundException {

        VerifierResponse verifierResponse;
        try {
            verifierResponse = verifierStrategy.verifyDetails(docOrchestratorRequest);
        } catch (TaskProcessingException e) {
            loggerUtil.error(e.getMessage());
            return;
        }

        VerificationEntity verification = docOrchestratorRequest.getVerification();

        Task task = docOrchestratorRequest.getTask();

        if(verifierResponse.getIsVerified()) {

            kycDocDetailsManager.saveKycDocDetails(docOrchestratorRequest.getKycDocDetails());

            task.setStatus(TaskStatus.SUCCESS);
            task.setRemarks("Successfully verified KYC Task: " + task.getType());
            taskManager.saveTask(task);

            updateVerificationEntityAndPushEvent(docOrchestratorRequest);

        } else {
            ErrorMessageRequest errorMessageRequest = ErrorMessageRequest.builder()
                    .verificationType(verification.getType())
                    .taskType(task.getType())
                    .comments(verifierResponse.getComments())
                    .build();
            String remarks = ErrorConstants.getErrorMessage(errorMessageRequest);
            task.setStatus(TaskStatus.PENDING_FOR_MANUAL_APPROVAL);
            task.setRemarks(remarks);
            taskManager.saveTask(task);

            verification.setStatus(VerificationStatus.PENDING_FOR_MANUAL_APPROVAL);
            verification.setRemarks(
                    StringUtils.hasLength(verification.getRemarks()) ? verification.getRemarks() + " :: " + remarks :
                    remarks);
            verificationManager.saveVerificationEntity(verification);

            List<Task> taskList = taskManager.findTasksByGroupId(verification.getId());

           // sendKafkaEvent(docOrchestratorRequest, taskList);
        }

    }

    private void updateVerificationEntityAndPushEvent(DocOrchestratorRequest docOrchestratorRequest) throws JsonProcessingException {

        VerificationEntity verification = docOrchestratorRequest.getVerification();
        Task currentTask = docOrchestratorRequest.getTask();

        List<Task> taskList = taskManager.findTasksByGroupId(verification.getId());

        Boolean allTaskSuccess = Boolean.TRUE;

        for(Task task : taskList) {
            if(currentTask != null && currentTask.getId().equals(task.getId())){
                task = currentTask;
            }
            if(!task.getStatus().equals(TaskStatus.SUCCESS)){
                allTaskSuccess = Boolean.FALSE;
                break;
            }
        }

        if(allTaskSuccess){
            verification.setStatus(VerificationStatus.SUCCESS);
            verificationManager.saveVerificationEntity(verification);
        }

        //sendKafkaEvent(docOrchestratorRequest, taskList);
    }

    /*public void sendKafkaEvent(DocOrchestratorRequest docOrchestratorRequest, List<Task> taskList) throws JsonProcessingException {

        List<DocVerificationResponseKafkaPayload.TaskDetails> taskDetailsList = new ArrayList<>();

        try{
            LoggerUtil.info("Pushing Kafka Event for Seller Onboarding with taskLists: " + taskList.toString());
        } catch (Exception ignored) {

        }


        for (Task t : taskList) {
            if(TaskStatus.PROCESSING.equals(t.getStatus())){
                return;
            }
            DocVerificationResponseKafkaPayload.TaskDetails taskDetails = DocVerificationResponseKafkaPayload.TaskDetails.builder()
                    .taskType(t.getType())
                    .docType(getDocTypeBasisOfTaskType(t.getType()))
                    .taskStatus(t.getStatus())
                    .remarks(t.getRemarks())
                    .build();
            taskDetailsList.add(taskDetails);
        }

        Verification verification = docOrchestratorRequest.getVerification();

        DocVerificationResponseKafkaPayload payload = DocVerificationResponseKafkaPayload.builder()
                .tenantId(verification.getTenantId())
                .verificationType(verification.getType())
                .status(verification.getStatus())
                .remarks(verification.getRemarks())
                .taskDetails(taskDetailsList)
                .build();

        kafkaProducer.sendMessage(kycAppProperties.getKafkaDocVerificationSellerOnboardingEventTopic(), verification.getTenantId(), objectMapper.writeValueAsString(payload));
        LoggerUtil.info("Pushed Kafka Event for Seller Onboarding with payload: " + objectMapper.writeValueAsString(payload));
    }*/

    private DocType getDocTypeBasisOfTaskType(TaskType taskType) {
        switch (taskType){
            case AADHAAR:
                return DocType.AADHAAR;
            case BANK_DETAILS:
                return DocType.BANK_CHEQUE;
            case GST:
                return DocType.GST;
            case PAN:
                return DocType.PAN;
            default:
                return null;
        }
    }
}
