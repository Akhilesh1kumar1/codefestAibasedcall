package com.sr.capital.kyc.service.constructor.response;


import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.AadhaarDocDetails;
import com.sr.capital.entity.mongo.kyc.child.BankDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.mongo.kyc.child.PanDocDetails;
import com.sr.capital.helpers.enums.DocActionType;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.response.*;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UploadExtractAndSaveResponseConstructor implements ResponseConstructor {

    final AES256 aes256;
    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        DocOrchestratorRequest orchestratorRequest = (DocOrchestratorRequest) input;
        log.info("[constructResponse] request {} ",orchestratorRequest);

        KycDocDetails<?> kycDocDetails = orchestratorRequest.getKycDocDetails();
        List<DocActionType> actionTypes = orchestratorRequest.getActions();

        List<String> images = new ArrayList<>();
        List<String> imageIds = new ArrayList<>();
        images.add(orchestratorRequest.getFile1().getPreSignedUri());
        imageIds.add(orchestratorRequest.getFile1().getFileName());
        if(orchestratorRequest.hasFile2()) {
            images.add(orchestratorRequest.getFile2().getPreSignedUri());
            imageIds.add(orchestratorRequest.getFile2().getFileName());
        }

        FetchDocDetailsResponse<?> docDetailsResponse = FetchDocDetailsResponse.builder()
                .srCompanyId(orchestratorRequest.getSrCompanyId())
                .docType(orchestratorRequest.getDocType())
                .images(images)
                .imageIds(imageIds)
                .status(kycDocDetails != null ? kycDocDetails.getStatus() : null)
                .details(actionTypes.contains(DocActionType.EXTRACT) ? generateDetailsBasisOfDocType(orchestratorRequest) : null)
                .build();

        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T) docDetailsResponse);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }


    @SuppressWarnings("unchecked")
    private <T> T generateDetailsBasisOfDocType(DocOrchestratorRequest orchestratorRequest) {
        DocType docType = orchestratorRequest.getDocType();
        switch (docType) {
            case BANK_CHEQUE:
                BankDocDetails bankDocDetails = ((KycDocDetails<BankDocDetails>) orchestratorRequest.getKycDocDetails()).getDetails();
                return (T) ExtractedBankResponse.builder()
                        .name(bankDocDetails.getAccountName())
                        .accountNo(bankDocDetails.getAccountNo())
                        .micrCode(bankDocDetails.getMicrCode())
                        .micrChequeNumber(bankDocDetails.getMicrChequeNumber())
                        .dateOfIssue(bankDocDetails.getDateOfIssue())
                        .bankName(bankDocDetails.getBankName())
                        .ifscCode(bankDocDetails.getIfscCode())
                        .bankAddress(bankDocDetails.getBankAddress())
                        .build();
            case GST:
                GstDocDetails gstDocDetails = ((KycDocDetails<GstDocDetails>)  orchestratorRequest.getKycDocDetails()).getDetails();
                ExtractedGstResponse gstResponse = ExtractedGstResponse.builder().build();
                gstResponse.setGstUserDetails(new ArrayList<>());
                gstDocDetails.getGstDetails().forEach(gstUserDetails -> {
                    gstResponse.getGstUserDetails().add(ExtractedGstResponse.GstUserDetails.builder().gstin(aes256.decrypt(gstUserDetails.getGstin())).username(aes256.decrypt(gstUserDetails.getUsername())).refId(gstUserDetails.getRefId()).build());
                });

                return (T) gstResponse;
            case PAN:
                PanDocDetails panDocDetails = ((KycDocDetails<PanDocDetails>) orchestratorRequest.getKycDocDetails()).getDetails();
                return (T) ExtractedPanResponse.builder()
                        .name(panDocDetails.getNameOnCard())
                        .fathersName(panDocDetails.getFathersName())
                        .dateOfBirth(panDocDetails.getDateOfBirth())
                        .age(panDocDetails.getAge())
                        .minor(panDocDetails.isMinor())
                        .idNumber(panDocDetails.getIdNumber())
                        .panType(panDocDetails.getPanType())
                        .dateOfIssue(panDocDetails.getDateOfIssue())
                        .build();
            case AADHAR:
                AadhaarDocDetails aadhaarDocDetails = ((KycDocDetails<AadhaarDocDetails>) orchestratorRequest.getKycDocDetails()).getDetails();
                return (T) ExtractedAadhaarResponse.builder()
                        .idNumber(aadhaarDocDetails.getIdNumber())
                        .name(aadhaarDocDetails.getNameOnCard())
                        .fathersName(aadhaarDocDetails.getFathersName())
                        .dateOfBirth(aadhaarDocDetails.getDateOfBirth())
                        .yearOfBirth(aadhaarDocDetails.getYearOfBirth())
                        .gender(aadhaarDocDetails.getGender())
                        .houseNumber(aadhaarDocDetails.getHouseNumber())
                        .streetAddress(aadhaarDocDetails.getStreetAddress())
                        .address(aadhaarDocDetails.getAddress())
                        .district(aadhaarDocDetails.getDistrict())
                        .state(aadhaarDocDetails.getState())
                        .pincode(aadhaarDocDetails.getPincode())
                        .build();
            default:
                return null;
        }
    }

}
