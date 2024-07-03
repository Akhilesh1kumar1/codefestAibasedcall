package com.sr.capital.kyc.service.constructor.response;

import com.amazonaws.HttpMethod;

import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.dto.response.*;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import com.sr.capital.util.S3Util;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class FetchDocDetailsResponseConstructor implements ResponseConstructor {


    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private AES256 aes256;


    @Override
    @SuppressWarnings("unchecked")
    public <T, U> ResponseEntity<GenericResponse<T>> constructResponse(U input) {

        List<KycDocDetails<?>> kycDocDetailsList = (List<KycDocDetails<?>>) input;

        Map<String, List<FetchDocDetailsResponse<?>>> tenantDocDetailsMap = new HashMap<>();

        for (KycDocDetails<?> kycDocDetails : kycDocDetailsList) {
            FetchDocDetailsResponse<?> docDetailsResponse = FetchDocDetailsResponse.builder()
                    .docType(kycDocDetails.getDocType())
                    .images(ObjectUtils.isEmpty(kycDocDetails.getImages()) ? null :  generatePreSignedUri(kycDocDetails.getImages()))
                    .imageIds(kycDocDetails.getImages())
                    .status(kycDocDetails.getStatus())
                    .details(getExtractedDetails(kycDocDetails))
                    .lastModifiedAt(String.valueOf(Optional.ofNullable(kycDocDetails.getLastModifiedAt())
                            .orElse(LocalDateTime.parse(kycDocDetails.getLastModifiedAt().toString()))))
                    .srCompanyId(kycDocDetails.getSrCompanyId())
                    .build();

            if(!tenantDocDetailsMap.containsKey(kycDocDetails.getSrCompanyId())) {
                tenantDocDetailsMap.put(kycDocDetails.getSrCompanyId(), new ArrayList<>());
            }

            tenantDocDetailsMap.get(kycDocDetails.getSrCompanyId()).add(docDetailsResponse);
        }

        GenericResponse<T> response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData((T) tenantDocDetailsMap);

        return new ResponseEntity<>(
                response,
                HttpStatus.OK
        );
    }


    private List<String> generatePreSignedUri(List<String> imageNames) {

        List<String> images = new ArrayList<>();
        for (String imageName : imageNames) {
            GeneratePreSignedUrlRequest preSignedUrlRequest = GeneratePreSignedUrlRequest.builder()
                    .filePath(imageName)
                    .bucketName(kycAppProperties.getBucketName())
                    .httpMethod(HttpMethod.GET)
                    .build();
            images.add(S3Util.generatePreSignedUrl(preSignedUrlRequest));
        }
        return  images;
    }




    @SuppressWarnings("unchecked")
    private <T> T getExtractedDetails(KycDocDetails<?> kycDocDetails) {
        switch(kycDocDetails.getDocType()){
            case AADHAAR:
                AadhaarDocDetails aadhaarDocDetails = (AadhaarDocDetails) kycDocDetails.getDetails();
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
            case PAN:
                PanDocDetails panDocDetails = (PanDocDetails) kycDocDetails.getDetails();
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
            case GST:
                GstDocDetails gstDocDetails = (GstDocDetails) kycDocDetails.getDetails();
                ExtractedGstResponse gstResponse = ExtractedGstResponse.builder().build();
                gstResponse.setGstUserDetails(new ArrayList<>());
                gstDocDetails.getGstDetails().forEach(gstUserDetails -> {
                    gstResponse.getGstUserDetails().add(ExtractedGstResponse.GstUserDetails.builder().gstin(aes256.decrypt(gstUserDetails.getGstin())).username(aes256.decrypt(gstUserDetails.getUsername())).refId(gstUserDetails.getRefId()).build());
                });

                return (T) gstResponse;
            case BANK_CHEQUE:
                BankDocDetails bankDocDetails = (BankDocDetails) kycDocDetails.getDetails();
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
            case GST_BY_PAN:
                GstByPanDocDetails gstByPanDocDetails = (GstByPanDocDetails) kycDocDetails.getDetails();
                return (T) gstByPanDocDetails;
            case MSME:
            case AGREEMENT:
            case CIN:
            case VOTER_ID:
            case PROPRIETORSHIP:
            case DRIVING_LICENSE:
                OldDocDetails oldDocDetails = (OldDocDetails) kycDocDetails.getDetails();
                if(oldDocDetails == null){
                    return null;
                }
                return (T) OldDocResponse.builder()
                        .docId(oldDocDetails.getDocId())
                        .build();
            default:
                return null;
        }
    }
}
