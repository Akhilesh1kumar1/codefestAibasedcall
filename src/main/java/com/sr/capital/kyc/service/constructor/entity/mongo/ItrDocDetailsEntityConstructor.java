package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.entity.mongo.kyc.child.ItrDocDetails;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.external.response.extraction.ItrExtractionResponseData;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import com.sr.capital.service.entityimpl.ItrDocDetailsServiceImpl;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import com.sr.capital.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItrDocDetailsEntityConstructor implements EntityConstructor {

    final AES256 aes256;
    final WebClientUtil webClientUtil;
    final AppProperties appProperties;
    final ItrDocDetailsServiceImpl itrDocDetailsService;
    final ItrDocDetailsServiceImpl itrDocHistoryService;

    LoggerUtil loggerUtil = LoggerUtil.getLogger(ItrDocDetailsEntityConstructor.class);

    @Override
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) throws IOException {

        ItrDocDetails itrDocDetails =null;
        try {
            itrDocDetails = MapperUtils.convertValue(request.getDocDetails(),ItrDocDetails.class);
        }catch (Exception ex){
           loggerUtil.error("error in doc parsing "+ex.getMessage());
        }

        itrDocDetails.setUsername(aes256.encrypt(itrDocDetails.getUsername()));
        itrDocDetails.setPassword(aes256.encrypt(itrDocDetails.getPassword()));
        KycDocDetails<ItrDocDetails> kycDocDetails = (KycDocDetails<ItrDocDetails>) request.getKycDocDetails();
        ItrExtractionResponseData itrExtractionResponseData = (ItrExtractionResponseData) request.getKarzaBaseResponse();
        List<String> images = new ArrayList<>();

        itrDocDetails.setRequestId(itrExtractionResponseData.getRequestId());
        if(itrExtractionResponseData.getResult()!=null ){
          extractResponseAndSaveDetails(itrExtractionResponseData,itrDocDetails,images,kycDocDetails==null?true:false);

        }

       if(kycDocDetails==null){
            kycDocDetails= KycDocDetails.<ItrDocDetails>builder()
                    .srCompanyId(RequestData.getTenantId())
                    .docType(DocType.GST).images(images)
                    .details(itrDocDetails)
                    .build();
        }
        return (T) kycDocDetails;
    }

    private void extractResponseAndSaveDetails(ItrExtractionResponseData itrExtractionResponseData, ItrDocDetails itrDocDetails,List<String> images,boolean isFirstTimeItrFetch) throws IOException {

       if(itrExtractionResponseData.getResult().getPdfDownloadLink()!=null) {
           String path = "/tmp/itr_" + itrDocDetails.getRequestId() + ".pdf";
           String fileName = "itr_" + itrDocDetails.getRequestId() + ".pdf";
           webClientUtil.downloadFileUsingUrl(itrExtractionResponseData.getResult().getPdfDownloadLink(), path);
           File file = new File(path);
           S3Util.uploadFileToS3(appProperties.getBucketName(), fileName, file);
           images.add(fileName);
           Files.deleteIfExists(Path.of(path));
       }
        com.sr.capital.entity.mongo.kyc.ItrDocDetails itrDocDetails1 = com.sr.capital.entity.mongo.kyc.ItrDocDetails.builder().itrExtractionData(itrExtractionResponseData).requestId(itrDocDetails.getRequestId()).srCompanyId(Long.valueOf(RequestData.getTenantId())).build();
        itrDocDetails1.setCreatedBy(String.valueOf(RequestData.getUserId()));
        itrDocDetails1.setLastModifiedBy(String.valueOf(RequestData.getUserId()));
        itrDocDetails.setFinancialYear(itrExtractionResponseData.getResult().getFormDetails().getFinancialYear());
        if(isFirstTimeItrFetch) {
            itrDocDetailsService.saveItrDoc(itrDocDetails1);
        }else{
            itrDocDetailsService.updateItr(itrDocDetails1);
        }
    }
}
