package com.sr.capital.kyc.service;

import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.UnsupportedMediaType;
import com.sr.capital.exception.custom.VerificationInProgressException;
import com.sr.capital.helpers.constants.DocExtractionConstants;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileNotFoundException;

@Service
public class ValidationService {

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;


    public void validateUploadAndExtractDocRequest(DocOrchestratorRequest orchestratorRequest)
        throws UnsupportedMediaType, VerificationInProgressException, FileNotFoundException {

        // KYC IN PROGRESS VALIDATION
        VerificationEntity verification = verificationManager
                .findInProgressVerificationByTenantIdAndVerificationType(RequestData.getTenantId(), VerificationType.DOC_VERIFICATION);
        if(verification != null) {
            throw new VerificationInProgressException();
        }
        FileDetails file1 =null;
        FileDetails file2 =null;
        if(orchestratorRequest.getIsFileRequired()) {
             file1 = orchestratorRequest.getFile1();
             file2 = orchestratorRequest.getFile2();

            // EMPTY FILE VALIDATION
            if (file1.getFile().getSize() == 0) {
                throw new FileNotFoundException();
            }

            // SUPPORTED FILE TYPES VALIDATION
            String file1Type = file1.getFile().getContentType();
            if (StringUtils.hasLength(file1Type) && !DocExtractionConstants.FileTypes.supportedFileTypes.contains(file1Type.toLowerCase())) {
                throw new UnsupportedMediaType();
            }
        }

        // AADHAAR SECOND FILE VALIDATION
        if(orchestratorRequest.getDocType().equals(DocType.AADHAR)){
            if(!orchestratorRequest.hasFile2() || file2.getFile().getSize() == 0){
                throw new FileNotFoundException();
            }

            String file2Type = file2.getFile().getContentType();
            if(StringUtils.hasLength(file2Type) && !DocExtractionConstants.FileTypes.supportedFileTypes.contains(file2Type.toLowerCase())) {
                throw new UnsupportedMediaType();
            }
        }

        KycDocDetails<?> kycDocDetails = kycDocDetailsManager
                .findKycDocDetailsByTenantIdAndDocType(RequestData.getTenantId(), orchestratorRequest.getDocType());

        orchestratorRequest.setKycDocDetails(kycDocDetails);
    }

    public void validateUploadExtractAndSaveDocRequest(DocOrchestratorRequest orchestratorRequest)
            throws CustomException, FileNotFoundException {

        //TODO: VALIDATE ACTIONS LIST

        if(orchestratorRequest.getDocType() == null) {
            throw new CustomException("INVALID REQUEST :: Doc Type is required!", HttpStatus.BAD_REQUEST);
        }
        if(!StringUtils.hasLength(orchestratorRequest.getSrCompanyId())){
            throw new CustomException("INVALID REQUEST :: Tenant Id is required!", HttpStatus.BAD_REQUEST);
        }
        if(CollectionUtils.isEmpty(orchestratorRequest.getActions())){
            throw new CustomException("INVALID REQUEST :: Actions are required!", HttpStatus.BAD_REQUEST);
        }

        FileDetails file1 = orchestratorRequest.getFile1();
        FileDetails file2 = orchestratorRequest.getFile2();

        // EMPTY FILE VALIDATION
        if(file1.getFile().getSize() == 0) {
            throw new FileNotFoundException();
        }

        // SUPPORTED FILE TYPES VALIDATION
        String file1Type = file1.getFile().getContentType();
        if(StringUtils.hasLength(file1Type) && !DocExtractionConstants.FileTypes.supportedFileTypes.contains(file1Type.toLowerCase())) {
            throw new UnsupportedMediaType();
        }

        // AADHAAR SECOND FILE VALIDATION
//        if(orchestratorRequest.getDocType().equals(DocType.AADHAAR)){
//            if(!orchestratorRequest.hasFile2() || file2.getFile().getSize() == 0){
//                throw new FileNotFoundException();
//            }
//
//            String file2Type = file2.getFile().getContentType();
//            if(StringUtils.hasLength(file2Type) && !DocExtractionConstants.FileTypes.supportedFileTypes.contains(file2Type.toLowerCase())) {
//                throw new UnsupportedMediaType();
//            }
//        }

        KycDocDetails<?> kycDocDetails = kycDocDetailsManager
                .findKycDocDetailsByTenantIdAndDocType(orchestratorRequest.getSrCompanyId(), orchestratorRequest.getDocType());

        orchestratorRequest.setKycDocDetails(kycDocDetails);
    }


   /* public void validateVerifyDetailsRequest(DocVerificationListenerRequest docVerificationListenerRequest) throws VerificationInProgressException, InvalidPayloadException {

        if(!StringUtils.hasLength(docVerificationListenerRequest.getTenantId())){
            throw new InvalidPayloadException("Tenant Id is required!");
        }

        if(CollectionUtils.isEmpty(docVerificationListenerRequest.getValidationList())){
            throw new InvalidPayloadException("Validation List can not be empty!");
        }

        Verification verification = verificationManager
                .findInProgressVerificationByTenantIdAndVerificationType(docVerificationListenerRequest.getTenantId(),
                        docVerificationListenerRequest.getVerificationType());

        if(verification != null) {
            throw new VerificationInProgressException();
        }
    }*/
}
