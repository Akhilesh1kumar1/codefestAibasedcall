package com.sr.capital.kyc.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.GstCompleteDocDetails;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.constants.DocExtractionConstants;
import com.sr.capital.helpers.constants.ErrorConstants;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.kyc.dto.request.BankDetailsRequest;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import com.sr.capital.kyc.dto.request.UpdateDocsDetailsRequest;
import com.sr.capital.kyc.dto.request.VerifyGstOtpRequest;
import com.sr.capital.kyc.dto.request.child.UpdateAadhaarDocDetailsRequest;
import com.sr.capital.kyc.dto.request.child.UpdateBankDocDetailsRequest;
import com.sr.capital.kyc.dto.request.child.UpdateGstDocDetailsRequest;
import com.sr.capital.kyc.dto.request.child.UpdatePanDocDetailsRequest;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.constructor.response.VerifiedDocResponseConstructor;
import com.sr.capital.kyc.service.strategy.ResponseConstructorStrategy;
import com.sr.capital.service.entityimpl.GstCompleteDetailsManager;
import com.sr.capital.service.entityimpl.TaskManager;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.util.LoggerUtil;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocDetailsService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private ResponseConstructorStrategy responseConstructorStrategy;

    private LoggerUtil logger =LoggerUtil.getLogger(DocDetailsService.class);

    @Autowired
    private VerifiedDocResponseConstructor verifiedDocResponseConstructor;

    @Autowired
    private GstCompleteDetailsManager gstCompleteDetailsManager;

    public ResponseEntity<?> fetchDocDetailsByTenantId(final DocDetailsRequest docDetailsRequest)
        throws CustomException {
        List<String> tenantIdList = docDetailsRequest.getSrCompanyId();
         RequestData.setRequestType(RequestType.DOC_DETAILS);
        if (ObjectUtils.isEmpty(tenantIdList) || tenantIdList.size() > DocExtractionConstants.LIST_SIZE) {
            throw new CustomException(ErrorConstants.FETCH_DOC_DETAILS_ERROR,
                HttpStatus.BAD_REQUEST);
        }

        boolean isInvalid = tenantIdList.stream().anyMatch(tId -> tId.trim().isBlank());
        if (isInvalid) {
            throw new CustomException(ErrorConstants.FETCH_DOC_DETAILS_ERROR,
                HttpStatus.BAD_REQUEST);
        }

        List<KycDocDetails<?>> kycDocDetailsList = kycDocDetailsManager.findKycDocDetailsByListOfTenantId(
            tenantIdList);

        return responseConstructorStrategy.constructResponse(kycDocDetailsList);
    }


    public ResponseEntity<?> fetchGstDocByTenantId(final DocDetailsRequest docDetailsRequest)
            throws CustomException {
        RequestData.setRequestType(RequestType.GST);
        List<String> tenantIdList = docDetailsRequest.getSrCompanyId();
        if (ObjectUtils.isEmpty(tenantIdList) || tenantIdList.size() > DocExtractionConstants.LIST_SIZE) {
            throw new CustomException(ErrorConstants.FETCH_DOC_DETAILS_ERROR,
                    HttpStatus.BAD_REQUEST);
        }

        boolean isInvalid = tenantIdList.stream().anyMatch(tId -> tId.trim().isBlank());
        if (isInvalid) {
            throw new CustomException(ErrorConstants.FETCH_DOC_DETAILS_ERROR,
                    HttpStatus.BAD_REQUEST);
        }

        List<GstCompleteDocDetails> gstCompleteDocDetails =new ArrayList<>();

        for(String tenantId:tenantIdList) {
            List<GstCompleteDocDetails> kycDocDetailsList = gstCompleteDetailsManager.getGstDetailsByTenantId(Long.valueOf(tenantId));
            gstCompleteDocDetails.addAll(kycDocDetailsList);
        }

        return responseConstructorStrategy.constructResponse(gstCompleteDocDetails);
    }


    public ResponseEntity<?> insertBankDetails(final BankDetailsRequest bankDetailsRequest)
        throws CustomException {
        BankDocDetails bankDocDetails = getBankExtractionResponse(bankDetailsRequest);
        KycDocDetails<?> bankDetails = KycDocDetails.<BankDocDetails>builder()
            .srCompanyId(RequestData.getTenantId())
            .docType(DocType.BANK_CHEQUE)
            .details(bankDocDetails)
            .build();

        KycDocDetails<?> kycDocDetails =  kycDocDetailsManager.findKycDocDetailsByTenantIdAndDocType(
            RequestData.getTenantId(), DocType.BANK_CHEQUE);

        if (ObjectUtils.isEmpty(kycDocDetails)) {
            kycDocDetailsManager.saveKycDocDetails(bankDetails);
        } else {
            KycDocDetails<BankDocDetails> newBankDocDetails = (KycDocDetails<BankDocDetails>) kycDocDetails;
            newBankDocDetails.setDetails(bankDocDetails);
            kycDocDetailsManager.saveKycDocDetails(newBankDocDetails);
        }

        return responseConstructorStrategy.constructResponse("Bank details saved successfully");
    }

    private BankDocDetails getBankExtractionResponse(final BankDetailsRequest bankDetailsRequest) {
        return BankDocDetails.builder()
            .accountName(bankDetailsRequest.getName())
            .accountNo(bankDetailsRequest.getAccountNumber())
            .bankName(bankDetailsRequest.getBankName())
            .ifscCode(bankDetailsRequest.getIfscCode())
            .build();
    }


   /* public DocVerificationResponseKafkaPayload updateDocDetails(UpdateDocsDetailsRequest updateDocsDetailsRequest) throws JsonProcessingException, IncompatibleDetailsException {

        Verification verification = verificationManager.findInProgressVerificationByTenantIdAndVerificationType(updateDocsDetailsRequest.getTenantId(), VerificationType.DOC_VERIFICATION);
        Verification bankVerification = verificationManager.findInProgressVerificationByTenantIdAndVerificationType(updateDocsDetailsRequest.getTenantId(), VerificationType.BANK_VERIFICATION);

        List<Task> taskList = new ArrayList<>();

        List<KycDocDetails<?>> kycDocDetailsList = updateKycDocsBasisOfDocType(updateDocsDetailsRequest);

        if("SUCCESS".equalsIgnoreCase(updateDocsDetailsRequest.getKycStatus())) {
            for(KycDocDetails<?> kycDocDetails : kycDocDetailsList) {
                kycDocDetails.setStatus(DocStatus.VERIFIED);
            }
            if(verification != null){
                taskList = taskManager.findTasksByGroupId(verification.getId());
                for(Task t: taskList) {
                    t.setStatus(TaskStatus.SUCCESS);
                }
                verification.setStatus(VerificationStatus.SUCCESS);
                verification.setRemarks("KYC approved manually!");
            }
            if(bankVerification != null){
                taskList = taskManager.findTasksByGroupId(bankVerification.getId());
                for(Task t: taskList) {
                    t.setStatus(TaskStatus.SUCCESS);
                }
                bankVerification.setStatus(VerificationStatus.SUCCESS);
                bankVerification.setRemarks("KYC approved manually!");
            }

        } else if ("FAILURE".equalsIgnoreCase(updateDocsDetailsRequest.getKycStatus())){
            for(KycDocDetails<?> kycDocDetails : kycDocDetailsList) {
                kycDocDetails.setStatus(DocStatus.UNVERIFIED);
            }
            if(verification != null){
                taskList = taskManager.findTasksByGroupId(verification.getId());
                for(Task t: taskList) {
                    t.setStatus(TaskStatus.FAILED);
                }
                verification.setStatus(VerificationStatus.FAILED);
                verification.setRemarks("KYC disapproved manually!");
            }
            if(bankVerification != null){
                taskList = taskManager.findTasksByGroupId(bankVerification.getId());
                for(Task t: taskList) {
                    t.setStatus(TaskStatus.FAILED);
                }
                bankVerification.setStatus(VerificationStatus.FAILED);
                bankVerification.setRemarks("KYC disapproved manually!");
            }
        }


        if(verification != null){
            verificationManager.saveVerification(verification);
        }
        if(bankVerification != null){
            verificationManager.saveVerification(bankVerification);
        }
        if(!CollectionUtils.isEmpty(taskList)) {
            taskManager.saveAllTasks(taskList);
        }

        kycDocDetailsManager.saveKycDocDetailsList(kycDocDetailsList);

        List<DocVerificationResponseKafkaPayload.TaskDetails> taskDetailsList = new ArrayList<>();
        for (Task t : taskList) {
            DocVerificationResponseKafkaPayload.TaskDetails taskDetails = DocVerificationResponseKafkaPayload.TaskDetails.builder()
                    .taskType(t.getType())
                    .docType(getDocTypeBasisOfTaskType(t.getType()))
                    .taskStatus(t.getStatus())
                    .remarks(t.getRemarks())
                    .build();
            taskDetailsList.add(taskDetails);
        }


        return DocVerificationResponseKafkaPayload.builder()
                .tenantId(updateDocsDetailsRequest.getTenantId())
                .status(verification != null ? verification.getStatus() : null)
                .remarks(verification != null ? verification.getRemarks() : null)
                .taskDetails(taskDetailsList)
                .build();

    }*/

    private List<KycDocDetails<?>> updateKycDocsBasisOfDocType(UpdateDocsDetailsRequest updateDocsDetailsRequest) throws JsonProcessingException {

        List<KycDocDetails<?>> kycDocDetailsList = new ArrayList<>();

        List<UpdateDocsDetailsRequest.DocDetails> docDetailsList = updateDocsDetailsRequest.getDocDetails();

        if(!CollectionUtils.isEmpty(docDetailsList)){

            for (UpdateDocsDetailsRequest.DocDetails<?> entry : docDetailsList) {

                DocType docType = entry.getDocType();
                KycDocDetails<?> kycDocDetails = kycDocDetailsManager
                        .findKycDocDetailsByTenantIdAndDocType(updateDocsDetailsRequest.getSrCompanyId(), docType);

                switch (docType) {
                    case AADHAAR:
                        UpdateAadhaarDocDetailsRequest ur = objectMapper.readValue(objectMapper.writeValueAsString(entry.getDetails()), UpdateAadhaarDocDetailsRequest.class);
                        if(ur == null){
                            if(!CollectionUtils.isEmpty(entry.getImageIds())) {
                                kycDocDetails.setImages(entry.getImageIds());
                                kycDocDetailsList.add(kycDocDetails);
                            }
                            break;
                        }
                        if(kycDocDetails != null){
                            AadhaarDocDetails aadhaarDocDetails = (AadhaarDocDetails) kycDocDetails.getDetails();
                            aadhaarDocDetails.setNameOnCard(StringUtils.hasLength(ur.getName()) ? ur.getName() : aadhaarDocDetails.getNameOnCard());
                            aadhaarDocDetails.setDateOfBirth(StringUtils.hasLength(ur.getDateOfBirth()) ? ur.getDateOfBirth() : aadhaarDocDetails.getDateOfBirth());
                            aadhaarDocDetails.setIdNumber(StringUtils.hasLength(ur.getIdNumber()) ? ur.getIdNumber() : aadhaarDocDetails.getIdNumber());
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());

                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            AadhaarDocDetails aadhaarDocDetails = AadhaarDocDetails.builder()
                                    .nameOnCard(StringUtils.hasLength(ur.getName()) ? ur.getName() : null)
                                    .dateOfBirth(StringUtils.hasLength(ur.getDateOfBirth()) ? ur.getDateOfBirth() : null)
                                    .idNumber(StringUtils.hasLength(ur.getIdNumber()) ? ur.getIdNumber() : null)
                                    .build();
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.AADHAAR)
                                    .details(aadhaarDocDetails)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }
                        break;
                    case BANK_CHEQUE:
                        UpdateBankDocDetailsRequest ub = objectMapper.readValue(objectMapper.writeValueAsString(entry.getDetails()), UpdateBankDocDetailsRequest.class);
                        if(ub == null){
                            if(!CollectionUtils.isEmpty(entry.getImageIds())) {
                                kycDocDetails.setImages(entry.getImageIds());
                                kycDocDetailsList.add(kycDocDetails);
                            }
                            break;
                        }
                        if(kycDocDetails != null) {
                            BankDocDetails bankDocDetails = (BankDocDetails) kycDocDetails.getDetails();
                            bankDocDetails.setAccountName(StringUtils.hasLength(ub.getAccountName()) ? ub.getAccountName() : bankDocDetails.getAccountName());
                            bankDocDetails.setAccountNo(StringUtils.hasLength(ub.getAccountNo()) ? ub.getAccountNo() : bankDocDetails.getAccountNo());
                            bankDocDetails.setMicrCode(StringUtils.hasLength(ub.getMicrCode()) ? ub.getMicrCode() : bankDocDetails.getMicrCode());
                            bankDocDetails.setBankName(StringUtils.hasLength(ub.getBankName()) ? ub.getBankName() : bankDocDetails.getBankName());
                            bankDocDetails.setIfscCode(StringUtils.hasLength(ub.getIfscCode()) ? ub.getIfscCode() : bankDocDetails.getIfscCode());
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());

                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            BankDocDetails bankDocDetails = BankDocDetails.builder()
                                    .accountName(StringUtils.hasLength(ub.getAccountName()) ? ub.getAccountName() : null)
                                    .accountNo(StringUtils.hasLength(ub.getAccountNo()) ? ub.getAccountNo() : null)
                                    .micrCode(StringUtils.hasLength(ub.getMicrCode()) ? ub.getMicrCode() : null)
                                    .bankName(StringUtils.hasLength(ub.getBankName()) ? ub.getBankName() : null)
                                    .ifscCode(StringUtils.hasLength(ub.getIfscCode()) ? ub.getIfscCode() : null)
                                    .build();
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.BANK_CHEQUE)
                                    .details(bankDocDetails)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }
                        break;
                    case GST:
                        UpdateGstDocDetailsRequest ug = objectMapper.readValue(objectMapper.writeValueAsString(entry.getDetails()), UpdateGstDocDetailsRequest.class);
                       /* if(ug == null){
                            if(!CollectionUtils.isEmpty(entry.getImageIds())) {
                                kycDocDetails.setImages(entry.getImageIds());
                                kycDocDetailsList.add(kycDocDetails);
                            }
                            break;
                        }
                        if(kycDocDetails != null) {
                            GstDocDetails gstDocDetails = (GstDocDetails) kycDocDetails.getDetails();
                            gstDocDetails.setLegalName(StringUtils.hasLength(ug.getLegalName()) ? ug.getLegalName() : gstDocDetails.getLegalName());
                            gstDocDetails.setTradeName(StringUtils.hasLength(ug.getTradeName()) ? ug.getTradeName() : gstDocDetails.getTradeName());
                            gstDocDetails.setGstin(StringUtils.hasLength(ug.getGstin()) ? ug.getGstin() : gstDocDetails.getGstin());
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());

                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            GstDocDetails gstDocDetails = GstDocDetails.builder()
                                    .legalName(StringUtils.hasLength(ug.getLegalName()) ? ug.getLegalName() : null)
                                    .tradeName(StringUtils.hasLength(ug.getTradeName()) ? ug.getTradeName() : null)
                                    .gstin(StringUtils.hasLength(ug.getGstin()) ? ug.getGstin() : null)
                                    .build();
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.GST)
                                    .details(gstDocDetails)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }*/
                        break;
                    case PAN:
                        UpdatePanDocDetailsRequest up = objectMapper.readValue(objectMapper.writeValueAsString(entry.getDetails()), UpdatePanDocDetailsRequest.class);
                        if (up == null){
                            if(!CollectionUtils.isEmpty(entry.getImageIds())) {
                                kycDocDetails.setImages(entry.getImageIds());
                                kycDocDetailsList.add(kycDocDetails);
                            }
                            break;
                        }
                        if(kycDocDetails != null) {
                            PanDocDetails panDocDetails = (PanDocDetails) kycDocDetails.getDetails();
                            panDocDetails.setNameOnCard(StringUtils.hasLength(up.getName()) ? up.getName() : panDocDetails.getNameOnCard());
                            panDocDetails.setDateOfBirth(StringUtils.hasLength(up.getDateOfBirth()) ? up.getDateOfBirth() : panDocDetails.getDateOfBirth());
                            panDocDetails.setIdNumber(StringUtils.hasLength(up.getIdNumber()) ? up.getIdNumber() : panDocDetails.getIdNumber());
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());

                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            PanDocDetails panDocDetails = PanDocDetails.builder()
                                    .nameOnCard(StringUtils.hasLength(up.getName()) ? up.getName() : null)
                                    .dateOfBirth(StringUtils.hasLength(up.getDateOfBirth()) ? up.getDateOfBirth() : null)
                                    .idNumber(StringUtils.hasLength(up.getIdNumber()) ? up.getIdNumber() : null)
                                    .build();
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.PAN)
                                    .details(panDocDetails)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }
                        break;
                    case MSME:
                        if(kycDocDetails != null) {
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());
                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.MSME)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }
                        break;
                    case AGREEMENT:
                        if(kycDocDetails != null) {
                            kycDocDetails.setImages(CollectionUtils.isEmpty(entry.getImageIds()) ? kycDocDetails.getImages() : entry.getImageIds());
                            kycDocDetailsList.add(kycDocDetails);
                        } else {
                            KycDocDetails<?> newDoc = KycDocDetails.builder()
                                    .srCompanyId(updateDocsDetailsRequest.getSrCompanyId())
                                    .docType(DocType.AGREEMENT)
                                    .images(CollectionUtils.isEmpty(entry.getImageIds()) ? null : entry.getImageIds())
                                    .build();
                            kycDocDetailsList.add(newDoc);
                        }
                        break;
                }
            }
        } else {
            kycDocDetailsList.addAll(kycDocDetailsManager.findKycDocDetailsByTenantId(updateDocsDetailsRequest.getSrCompanyId()));
        }
        return kycDocDetailsList;
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

    public ResponseEntity<?> verifiedDocData(final String tenantId)throws CustomException {
        if(!StringUtils.hasLength(tenantId.trim())) {
            throw new CustomException("Invalid tenant Id!", HttpStatus.BAD_REQUEST);
        }

        return verifiedDocResponseConstructor.constructVerifiedResponse(tenantId);
    }

}
