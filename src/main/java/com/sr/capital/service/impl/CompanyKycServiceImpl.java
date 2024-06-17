package com.sr.capital.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.dto.request.UpdateCompanyKycDetails;
import com.sr.capital.dto.response.CompanyKycDetailsResponse;
import com.sr.capital.entity.primary.CompanyKycDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.ProofOfIdentity;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.repository.primary.CompanyKycRepository;
import com.sr.capital.service.CompanyKycService;
import com.sr.capital.service.strategy.RequestValidationStrategy;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.sr.capital.helpers.constants.Constants.ServiceConstants.ADHAR_IMAGE_FOLDER_NAME;

@Service
@RequiredArgsConstructor
public class CompanyKycServiceImpl implements CompanyKycService {

    final RequestValidationStrategy requestValidationStrategy;
    final AppProperties appProperties;
    final CompanyKycRepository companyKycRepository;
    @Override
    public CompanyKycDetailsResponse saveCompanyKycDetais(CompanyKycDetailsRequest companyKycDetailsRequest, MultipartFile multipartFile) throws Exception {
        requestValidationStrategy.validateRequest(companyKycDetailsRequest, RequestType.COMPANY_KYC);
        CompanyKycDetails companyKycDetails  = MapperUtils.convertValue(companyKycDetailsRequest,CompanyKycDetails.class);
        companyKycDetails =companyKycRepository.save(companyKycDetails);
        AtomicInteger atomicInteger = new AtomicInteger();
        String imageUrl = updateAndGetImageUrl(multipartFile,companyKycDetails,atomicInteger,companyKycDetails.getProofOfIdentity().name());
        companyKycDetails.setProofOfIdentityImageLink(imageUrl);
        return MapperUtils.convertValue(companyKycDetails,CompanyKycDetailsResponse.class);
    }

    @Override
    public CompanyKycDetailsResponse updateCompanyKycDetails(UpdateCompanyKycDetails companyKycDetailsRequest, List<MultipartFile> multipartFileList) throws CustomException, IOException {

        Optional<CompanyKycDetails> optionalCompanyKycDetails = companyKycRepository.findById(companyKycDetailsRequest.getId());
        CompanyKycDetails companyKycDetails =null;

        if(optionalCompanyKycDetails.isPresent()){

            companyKycDetails=optionalCompanyKycDetails.get();
            updateKycDetailsAccordingToDocumentType(companyKycDetailsRequest,multipartFileList,companyKycDetails);
            companyKycRepository.save(companyKycDetails);

        }else{
            throw new CustomException("Company details  not found", HttpStatus.BAD_REQUEST);
        }
        return MapperUtils.convertValue(companyKycDetails,CompanyKycDetailsResponse.class);
    }

    private void updateKycDetailsAccordingToDocumentType(UpdateCompanyKycDetails companyKycDetailsRequest, List<MultipartFile> multipartFileList,CompanyKycDetails companyKycDetails){
        switch (companyKycDetailsRequest.getDocumentType()){
            case ADDRESS_PROOF -> {
                companyKycDetails.setProofOfAddress(companyKycDetailsRequest.getProofOfAddress());
                companyKycDetails.setAddressProofLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));
            }
            case BUSINESS_PROOF, PROOF_OF_BUSINESS -> {

                companyKycDetails.setProofOfBusiness(companyKycDetailsRequest.getProofOfBusiness());
                companyKycDetails.setBusinessProofLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));
            }
            case PARTNERSHIP_DEED -> companyKycDetails.setPartnerShipDeedLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                    .collect(Collectors.joining(", ")));
            case IDENTITY_PROOF -> {
                companyKycDetails.setProofOfIdentity(companyKycDetailsRequest.getProofOfIdentity());
                companyKycDetails.setProofOfIdentityImageLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));
            }
            case BUSINESS_ENTITY_PAN -> companyKycDetails.setPanOfBusinessEntity(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                    .collect(Collectors.joining(", ")));
            case CERT_OF_INCORPORATION -> companyKycDetails.setCertificateOfIncorporationLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                    .collect(Collectors.joining(", ")));
            case CURRENT_ADDRESS_PROOF -> {

                companyKycDetails.setProofOfCurrentAddressLink(companyKycDetails.getProofOfCurrentAddressLink());
                companyKycDetails.setProofOfCurrentAddressLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));
            }
            case POA_GRADE_TO_TRANSACT -> {
                companyKycDetails.setPoaGradeToTransact(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));
            }
            case ARTICLE_OF_ASSOCIATION -> {
                companyKycDetails.setArticleOfAssociationLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                        .collect(Collectors.joining(", ")));

            }
            case REGISTRATION_CERTIFICATE -> companyKycDetails.setRegistrationCertificateLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                    .collect(Collectors.joining(", ")));
            case MEMORANDUM_OF_ASSOCIATION -> companyKycDetails.setMemorandumOfAssociationLink(updateImages(multipartFileList,companyKycDetails,companyKycDetailsRequest.getDocumentType().name()).stream()
                    .collect(Collectors.joining(", ")));
            default -> {
            }
        }
    }

    private String updateAndGetImageUrl(MultipartFile multipartFile, CompanyKycDetails companyKycDetails, AtomicInteger imageNumber,String folderName){
        return S3Util.uploadDocument(multipartFile,appProperties.getBucketName(),String.valueOf(companyKycDetails.getUserId()),folderName ,String.valueOf(companyKycDetails.getId()),imageNumber);
    }

    private List<String> updateImages(List<MultipartFile> multipartFileList ,CompanyKycDetails companyKycDetails,String folderName){
        List<String> imagesUrls =new ArrayList<>();
        AtomicInteger imageNumber =new AtomicInteger();

        multipartFileList.forEach(multipartFile -> {
            String url = S3Util.uploadDocument(multipartFile,appProperties.getBucketName(),String.valueOf(companyKycDetails.getUserId()),folderName ,String.valueOf(companyKycDetails.getId()),imageNumber);
            imagesUrls.add(url);
        });
        return imagesUrls;
    }
}
