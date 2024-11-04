package com.sr.capital.service.impl;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.ReportMetaData;
import com.sr.capital.external.common.request.DocumentUploadRequestDto;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentSyncHelperServiceImpl {

    final DocDetailsService docDetailsService;
    final RedissonClient redissonClient;
    final FileUploadService fileUploadService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final AppProperties appProperties;
    @Async
    public void syncDocumentToVendor(LoanMetaDataDto loanMetaDataDto) {

        List<KycDocDetails<?>> kycDocDetailsList = docDetailsService.fetchDocDetailsByTenantId(String.valueOf(loanMetaDataDto.getSrCompanyId()));


        if (CollectionUtils.isNotEmpty(kycDocDetailsList)) {

            for (KycDocDetails<?> kycDocDetails : kycDocDetailsList) {
                if (kycDocDetails.getDocType().name().equalsIgnoreCase(DocType.BUSINESS_ADDRESS.name()) || kycDocDetails.getDocType().name().equalsIgnoreCase(DocType.PERSONAL_ADDRESS.name())) {
                    log.info("Document Sync not required");
                } else {

                    String key = loanMetaDataDto.getSrCompanyId() + "_" + kycDocDetails.getDocType();
                    RMapCache<String, Boolean> documentCacheDetails = redissonClient
                            .getMapCache(key);
                    if (CollectionUtils.isNotEmpty(kycDocDetails.getImages())) {
                        if (!documentCacheDetails.get(key) && kycDocDetails.getDetails() instanceof ReportMetaData) {
                            ReportMetaData reportMetaData = (ReportMetaData) kycDocDetails.getDetails();
                            for (String image : kycDocDetails.getImages()) {
                                try (InputStream inputStream = S3Util.downloadObjectToFile(appProperties.getBucketName(), image)) {
                                    if (inputStream != null) {
                                        String documentType = (String) reportMetaData.getMetaData().get("document_type");
                                        String documentCategory = (String) reportMetaData.getMetaData().get("document_category");
                                        Map<String, String> metaData = (Map<String, String>) reportMetaData.getMetaData().get("meta_data");

                                        if (documentType != null && documentCategory != null && metaData != null) {
                                            DocumentUploadRequestDto documentUploadRequestDto = DocumentUploadRequestDto.builder()
                                                    .documentType(documentType)
                                                    .documentCategory(documentCategory)
                                                    .fileName(image)
                                                    .inputStream(inputStream)
                                                    .metaData(metaData)
                                                    .key(key)
                                                    .build();

                                            // Handle multiple uploads properly (e.g., append to a list)
                                            loanMetaDataDto.setDocumentUploadRequestDtos(documentUploadRequestDto);

                                            // Call the partner service to upload the document
                                            creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).uploadDocument(loanMetaDataDto);
                                        } else {
                                            log.error("Missing required metaData for document upload.");
                                        }
                                   }
                                } catch (IOException e) {
                                    //throw new RuntimeException(e);
                                    log.error("error in document sync{} ", e);
                                }
                            }
                        }
                    }
                }

            /*if(CollectionUtils.isNotEmpty(documentUploadRequestDtos)){
                loanMetaDataDto.setDocumentUploadRequestDtos(documentUploadRequestDtos);
                creditPartnerFactoryService.getPartnerService(loanMetaDataDto.getLoanVendorName()).uploadDocument(loanMetaDataDto);
            }else{
                log.info("No document found for upload company {} ",loanMetaDataDto.getSrCompanyId());
            }*/

            }

        }
    }
}
