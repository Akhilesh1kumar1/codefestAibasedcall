package com.sr.capital.kyc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.omunify.interceptor.utils.ThreadContextUtil;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.primary.Task;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.KycVerifiedDetails;
import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.external.response.verification.*;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.manager.KycVerifiedDetailsManager;
import com.sr.capital.kyc.service.strategy.EntityConstructorStrategy;
import com.sr.capital.service.entityimpl.TaskManager;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class KarzaResponseProcessor {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private KycVerifiedDetailsManager kycVerifiedDetailsManager;

    @Autowired
    private EntityConstructorStrategy entityConstructorStrategy;

    @Autowired
    private DetailsComparator detailsComparator;


    public void processKarzaResponse(String message) throws IOException, CustomException {

        List<KarzaBaseResponse<?>> idfyGenericResponseList = Arrays.asList(objectMapper.readValue(message, KarzaBaseResponse[].class));

        KarzaBaseResponse<?> idfyGenericResponse = idfyGenericResponseList.get(0);

        VerificationEntity verification = verificationManager.findByVerificationId(Long.valueOf(idfyGenericResponse.getGroupId()));

        RequestData.setTenantId(verification.getSrCompanyId());

        Task task = taskManager.findTaskByTaskId(Long.valueOf(idfyGenericResponse.getTaskId()));

        KarzaBaseResponse<?> typedIdfyGenericResponse = getTypedKarzaGenericResponse(message, task.getType());

        KycDocDetails<?> kycDocDetails = kycDocDetailsManager
                .findKycDocDetailsByTenantIdAndDocType(verification.getSrCompanyId(), getDocTypeBasisOfTaskType(task.getType()));

        DocOrchestratorRequest docOrchestratorRequest = DocOrchestratorRequest.builder()
                .verification(verification)
                .srCompanyId(verification.getSrCompanyId())
                .task(task)
                .karzaBaseResponse(typedIdfyGenericResponse)
                .kycDocDetails(kycDocDetails)
                .build();

        KycVerifiedDetails<?> kycVerifiedDetails = kycVerifiedDetailsManager
                .findKycVerifiedDetailsByTenantIdAndTaskType(verification.getSrCompanyId(), task.getType());

        kycVerifiedDetails = entityConstructorStrategy.constructEntity(docOrchestratorRequest, kycVerifiedDetails, getResponseClass(task.getType()));

        kycVerifiedDetailsManager.saveKycVerifiedDetails(kycVerifiedDetails);

        docOrchestratorRequest.setKycVerifiedDetails(kycVerifiedDetails);

        detailsComparator.verifyDetailsAndUpdateTasks(docOrchestratorRequest);

        ThreadContextUtil.clearThreadContext();

    }


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


    private Class<?> getResponseClass(TaskType taskType) throws CustomException {
        switch (taskType){
            case AADHAAR:
                return AadhaarVerifiedDetails.class;
            case BANK_DETAILS:
                return BankVerifiedDetails.class;
            case GST:
                return GstVerifiedDetails.class;
            case PAN_AADHAAR:
                return PanAadhaarCrossVerifiedDetails.class;
            case PAN_GST:
                return PanGstCrossVerifiedDetails.class;
            case PAN:
                return PanVerifiedDetails.class;
            default:
                throw new CustomException("Invalid Doc Type!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private KarzaBaseResponse<?> getTypedKarzaGenericResponse(String message, TaskType taskType) throws JsonProcessingException {

        switch (taskType) {
            case PAN:
                return Arrays.asList(objectMapper.readValue(message, PanCardVerificationResponse[].class)).get(0);
            case GST:
                return Arrays.asList(objectMapper.readValue(message, GstVerificationResponse[].class)).get(0);
            case BANK_DETAILS:
                return Arrays.asList(objectMapper.readValue(message, BankVerificationResponse[].class)).get(0);
            case PAN_AADHAAR:
                return Arrays.asList(objectMapper.readValue(message, PanAadhaarCrossVerificationResponse[].class)).get(0);
            case PAN_GST:
                return Arrays.asList(objectMapper.readValue(message, PanGstCrossVerificationResponse[].class)).get(0);
            case AADHAAR:
                return Arrays.asList(objectMapper.readValue(message, AadhaarVerificationResponse[].class)).get(0);
        }
        return objectMapper.readValue(message, KarzaBaseResponse.class);
    }
}
