package com.sr.capital.kyc.service.constructor.response;

import com.amazonaws.HttpMethod;

import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.dto.response.*;
import com.sr.capital.kyc.external.request.extraction.data.ItrExtractionData;
import com.sr.capital.kyc.service.interfaces.ResponseConstructor;
import com.sr.capital.util.S3Util;
import org.apache.commons.collections.CollectionUtils;
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
                    .kycType(kycDocDetails.getKycType())
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
            if(kycAppProperties.getActiveProfile().equalsIgnoreCase("local")){
                return images;
            }
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
           /* case AADHAAR:
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
                    gstResponse.getGstUserDetails().add(ExtractedGstResponse.GstUserDetails.builder().gstin(aes256.decrypt(gstUserDetails.getGstin())).username(aes256.decrypt(gstUserDetails.getUsername())).refId(gstUserDetails.getRefId()).status(gstUserDetails.getStatus()).build());
                });

                return (T) gstResponse;*/
            case BANK_CHEQUE:
                List<ExtractedBankResponse> extractedBankResponses = getExtractedBankResponses(kycDocDetails);
                return (T) extractedBankResponses;
            case GST_BY_PAN:
                GstByPanDocDetails gstByPanDocDetails = (GstByPanDocDetails) kycDocDetails.getDetails();
                return (T) gstByPanDocDetails;
            case ITR:
                ItrExtractionData itrExtractionData = getItrData(kycDocDetails);
                return (T) itrExtractionData;
            case MSME:
            case PROVISIONAL:
            case LOAN_TRACKER:
            case DRIVING_LICENCE:
            case PROPRIETORSHIP:
            case VOTING_CARD:
            case CIN:
            case AGREEMENT:
            case DIRECTORS:
            case PAN_GUARANTOR:
            case PAN_PERSONAL:
            case PASSPORT:
            case ADHAR_GUARANTOR_COAPPLICANT:
            case GST_REGISTRATION:
            case SHOP_EST_REGISTRATION:
            case TRADE_LICENSE:
            case FOOD_LICENSE:
            case DRUG_LICENSE_CERTIFICATE:
            case UDYAM_REGISTRATION:
            case UDYOG_AADHAAR:
            case BANK_STATEMENT_CURRENT_6:
            case ELECTRICITY_COMPANY:
            case SALE_DEED_COMPANY:
            case LANDLINE_BILL_3MONTH:
            case PROPERTY_TAX_RECEIPT:
            case RENT_AGREEMENT_COMPANY:
            case FINANCIAL_AUDIT:
            case ITR_RETURNS:
            case GST_RETURNS_6:
            case VALID_PARTNERSHIP_DEED:
            case COMPANY_PAN:
            case COMPANY_COI:
            case MOA_AOA_COMPANY:
            case LATEST_CA_SHAREHOLDINGS:
            case ELECTRICITY:
            case PIPED_GAS_BILL:
            case WATER_BILL:
            case SALE_DEED:
            case LANDLINE_BILL:
            case PAN:
            case AADHAR:
            case GST:
            case EPAN:
            case TRADE_CERTIFICATE:
                return (T) kycDocDetails.getDetails();
            case PERSONAL_ADDRESS:
                PersonalAddressDetails personalAddressDetails = (PersonalAddressDetails) kycDocDetails.getDetails();
                personalAddressDetails.getAddress().forEach(personalAddress->{
                    personalAddress.setAddress1(aes256.decrypt(personalAddress.getAddress1()));
                    personalAddress.setAddress2(aes256.decrypt(personalAddress.getAddress2()));
                    personalAddress.setCity(aes256.decrypt(personalAddress.getCity()));
                    personalAddress.setState(aes256.decrypt(personalAddress.getState()));
                    personalAddress.setPincode(aes256.decrypt(personalAddress.getPincode()));
                    personalAddress.setOwnershipStatus(personalAddress.getOwnershipStatus());
                });
                personalAddressDetails.setKycType(kycDocDetails.getKycType());
                return (T) personalAddressDetails;
            case BUSINESS_ADDRESS:
                 BusinessAddressDetails businessAddressDetails = (BusinessAddressDetails) kycDocDetails.getDetails();
                 businessAddressDetails.setBusinessPanNumber(aes256.decrypt(businessAddressDetails.getBusinessPanNumber()));
                 businessAddressDetails.setAddress1(aes256.decrypt(businessAddressDetails.getAddress1()));
                 businessAddressDetails.setAddress2(aes256.decrypt(businessAddressDetails.getAddress2()));
                 businessAddressDetails.setCity(aes256.decrypt(businessAddressDetails.getCity()));
                 businessAddressDetails.setState(aes256.decrypt(businessAddressDetails.getState()));
                 businessAddressDetails.setPincode(aes256.decrypt(businessAddressDetails.getPincode()));
                 buildPartnerInfo(businessAddressDetails);
                 return (T) businessAddressDetails;
            /*case AGREEMENT:
            case CIN:
            case VOTING_CARD:
            case PROPRIETORSHIP:
            case DRIVING_LICENCE:
                OldDocDetails oldDocDetails = (OldDocDetails) kycDocDetails.getDetails();
                if(oldDocDetails == null){
                    return null;
                }
                return (T) OldDocResponse.builder()
                        .docId(oldDocDetails.getDocId())
                        .build();*/
            default:
                return null;
        }
    }

    private void buildPartnerInfo(BusinessAddressDetails businessAddressDetails) {

        if(CollectionUtils.isNotEmpty(businessAddressDetails.getBusinessPartnerInfo())){
            businessAddressDetails.getBusinessPartnerInfo().forEach(partnerInfoDto->{
                partnerInfoDto.setDob(aes256.decrypt(partnerInfoDto.getDob()));
                partnerInfoDto.setAddress(aes256.decrypt(partnerInfoDto.getAddress()));
                partnerInfoDto.setName(aes256.decrypt(partnerInfoDto.getName()));
                partnerInfoDto.setGender(partnerInfoDto.getGender());
                partnerInfoDto.setMobileNumber(aes256.decrypt(partnerInfoDto.getMobileNumber()));
                partnerInfoDto.setPincode(aes256.decrypt(partnerInfoDto.getPincode()));
                partnerInfoDto.setPanNumber(aes256.decrypt(partnerInfoDto.getPanNumber()));
                partnerInfoDto.setBusinessPartnerHolding(aes256.decrypt(partnerInfoDto.getBusinessPartnerHolding()));
            });
        }
    }

    private ItrExtractionData getItrData(KycDocDetails<?> kycDocDetails) {
        ItrDocDetails itrDocDetails = (ItrDocDetails) kycDocDetails.getDetails();
        ItrExtractionData itrExtractionData =ItrExtractionData.builder().username(aes256.decrypt(itrDocDetails.getUsername())).password(aes256.decrypt(itrDocDetails.getPassword())).build();
        return itrExtractionData;
    }

    private List<ExtractedBankResponse> getExtractedBankResponses(KycDocDetails<?> kycDocDetails) {
        List<BankDocDetails> bankDocDetailsList = (List<BankDocDetails>) kycDocDetails.getDetails();
        List<ExtractedBankResponse> extractedBankResponses =new ArrayList<>();
        bankDocDetailsList.forEach(bankDocDetails -> {
            extractedBankResponses.add( ExtractedBankResponse.builder()
                    .name(aes256.decrypt(bankDocDetails.getAccountName()))
                    .accountNo(aes256.decrypt(bankDocDetails.getAccountNo()))
                    .micrCode(bankDocDetails.getMicrCode())
                    .micrChequeNumber(bankDocDetails.getMicrChequeNumber())
                    .dateOfIssue(bankDocDetails.getDateOfIssue())
                    .bankName(bankDocDetails.getBankName())
                    .ifscCode(bankDocDetails.getIfscCode())
                    .bankAddress(aes256.decrypt(bankDocDetails.getBankAddress()))
                            .bankAccountType(bankDocDetails.getBankAccountType())
                    .build());
        });
        return extractedBankResponses;
    }
}
