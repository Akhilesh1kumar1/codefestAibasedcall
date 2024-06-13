package com.sr.capital.kyc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class DocVerificationService {

    @Autowired
    private ValidationService validationService;

    /*@Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private KycVerifiedDetailsManager kycVerifiedDetailsManager;

    @Autowired
    private VerificationManager verificationManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private IdfyVerificationAdapter idfyVerificationAdapter;

    @Autowired
    private DetailsComparator detailsComparator;

    @Autowired
    private ExternalRequestTransformerStrategy externalRequestTransformerStrategy;

*/
   /* public void initiateVerification(DocVerificationListenerRequest docVerificationListenerRequest)
            throws IncompatibleDetailsException, JsonProcessingException, VerifierNotFound,
            ServiceEndpointNotFoundException, RequestTransformerNotFoundException, IDfyNameComparisonException {

        ThreadContextUtil.setTenantId(docVerificationListenerRequest.getTenantId());

        try {
            validationService.validateVerifyDetailsRequest(docVerificationListenerRequest);
        } catch (VerificationInProgressException | InvalidPayloadException exception){
            LoggerUtil.error("Verification could not be initiated for tenant id: " + docVerificationListenerRequest.getTenantId() +
                    " Exception: " + exception.getMessage());
            return;
        }

        Verification verification = Verification.builder()
                .tenantId(docVerificationListenerRequest.getTenantId())
                .type(docVerificationListenerRequest.getVerificationType())
                .build();

        verificationManager.saveVerification(verification);

        Map<DocType, KycDocDetails<?>> docTypeKycDocDetailsMap = getKycDocTypeDetailsMap(docVerificationListenerRequest.getTenantId());

        Map<TaskType, Pair<KycDocDetails<?>, KycVerifiedDetails<?>>> taskTypeKycVerifiedDetailsMap =
                getStoredKycVerifiedDetails(docVerificationListenerRequest.getTenantId(), docTypeKycDocDetailsMap);

        List<TaskType> taskList = docVerificationListenerRequest.getValidationList();

        Map<Task, CompletableFuture<IdfyAsyncResponse>> taskCompletableFutureMap = new HashMap<>();

        for (TaskType t : taskList) {

            Task task = constructAndSaveTaskEntity(verification, t);

            if (t.equals(TaskType.PAN_BANK_DETAILS)) {

                DocOrchestratorRequest docOrchestratorRequest = DocOrchestratorRequest.builder()
                        .docTypeKycDocDetailsMap(docTypeKycDocDetailsMap)
                        .verification(verification)
                        .tenantId(docVerificationListenerRequest.getTenantId())
                        .task(task)
                        .build();

                detailsComparator.verifyDetailsAndUpdateTasks(docOrchestratorRequest);
                continue;
            }

            if (t.equals(TaskType.AADHAAR)) {

                KycDocDetails<?> kycDocDetails = docTypeKycDocDetailsMap.get(DocType.AADHAAR);

                DocOrchestratorRequest docOrchestratorRequest = DocOrchestratorRequest.builder()
                        .kycDocDetails(kycDocDetails)
                        .verification(verification)
                        .tenantId(verification.getTenantId())
                        .task(task)
                        .build();

                detailsComparator.verifyDetailsAndUpdateTasks(docOrchestratorRequest);
                continue;

            }

            if(taskTypeKycVerifiedDetailsMap.containsKey(t)) {

                Pair<KycDocDetails<?>, KycVerifiedDetails<?>> kycDetailsPair = taskTypeKycVerifiedDetailsMap.get(t);

                DocOrchestratorRequest docOrchestratorRequest = DocOrchestratorRequest.builder()
                        .verification(verification)
                        .tenantId(docVerificationListenerRequest.getTenantId())
                        .task(task)
                        .kycDocDetails(kycDetailsPair.getFirst())
                        .kycVerifiedDetails(kycDetailsPair.getSecond())
                        .build();

                detailsComparator.verifyDetailsAndUpdateTasks(docOrchestratorRequest);
                continue;

            }

            KycDocDetails<?> kycDocDetails = docTypeKycDocDetailsMap.get(TypeUtil.getDocTypeBasisOfTaskType(t));

            DocOrchestratorRequest docOrchestratorRequest = DocOrchestratorRequest.builder()
                    .verification(verification)
                    .tenantId(docVerificationListenerRequest.getTenantId())
                    .task(task)
                    .kycDocDetails(kycDocDetails)
                    .build();

            IdfyBaseRequest<?> idfyBaseRequest = externalRequestTransformerStrategy.transformVerificationRequest(docOrchestratorRequest);
            CompletableFuture<IdfyAsyncResponse> verificationResponse = idfyVerificationAdapter.verifyDocuments(idfyBaseRequest);
            taskCompletableFutureMap.put(task, verificationResponse);

        }


        try {
            List<Task> verifiedTasks = new ArrayList<>();
            for(Task t : taskCompletableFutureMap.keySet()) {
                t.setRequestId(taskCompletableFutureMap.get(t).get().getRequestId());
                verifiedTasks.add(t);
            }
            taskManager.saveAllTasks(verifiedTasks);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ThreadContextUtil.clearThreadContext();

    }


    private Map<DocType, KycDocDetails<?>> getKycDocTypeDetailsMap(String tenantId) {

        Map<DocType, KycDocDetails<?>> docTypeKycDocDetailsMap = new HashMap<>();
        List<KycDocDetails<?>> kycDocDetailsList = kycDocDetailsManager.findKycDocDetailsByTenantId(tenantId);
        for (KycDocDetails<?> entry : kycDocDetailsList){
            docTypeKycDocDetailsMap.put(entry.getDocType(), entry);
        }
        return docTypeKycDocDetailsMap;
    }



    private Task constructAndSaveTaskEntity(Verification verification, TaskType taskType) {
        Task task = Task.builder()
                .requestId(UUID.randomUUID().toString())
                .groupId(verification.getId())
                .type(taskType)
                .build();
        return taskManager.saveTask(task);
    }



    private Map<TaskType, Pair<KycDocDetails<?>, KycVerifiedDetails<?>>> getStoredKycVerifiedDetails(String tenantId,
                                                                                                     Map<DocType, KycDocDetails<?>> docTypeKycDocDetailsMap) {

        Map<TaskType, Pair<KycDocDetails<?>, KycVerifiedDetails<?>>> taskTypeKycVerifiedDetailsMap = new HashMap<>();

        List<KycVerifiedDetails<?>> kycVerifiedDetailsList = kycVerifiedDetailsManager.findKycVerifiedDetailsByTenantId(tenantId);

        if(!CollectionUtils.isEmpty(kycVerifiedDetailsList)) {

            for (KycVerifiedDetails<?> entry : kycVerifiedDetailsList) {

                if(entry.getExpiresAt().isAfter(LocalDateTime.now())) {

                    KycDocDetails<?> kycDocDetails = docTypeKycDocDetailsMap.get(TypeUtil.getDocTypeBasisOfTaskType(entry.getTaskType()));

                    if(kycDocDetails != null) {
                        taskTypeKycVerifiedDetailsMap.put(entry.getTaskType(), Pair.of(kycDocDetails, entry));
                    }
                }
            }
        }
        return taskTypeKycVerifiedDetailsMap;
    }*/



}
