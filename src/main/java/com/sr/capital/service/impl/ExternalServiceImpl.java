package com.sr.capital.service.impl;

import com.amazonaws.HttpMethod;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.dto.request.UploadFileToS3Request;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.service.CapitalDataReportService;
import com.sr.capital.service.CreditPartnerFactoryService;
import com.sr.capital.service.ExternalService;
import com.sr.capital.service.LoanOfferService;
import com.sr.capital.util.CsvUtils;
import com.sr.capital.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalServiceImpl implements ExternalService {

    final LoanOfferService loanOfferService;
    final CreditPartnerFactoryService creditPartnerFactoryService;
    final CapitalDataReportService capitalDataReportService;
    final CsvUtils csvUtils;
    final AppProperties appProperties;

    final DocDetailsService docDetailsService;
    @Override
    public Boolean validateRequest(String vendorToken, String vendorCode,String loanVendorName) throws InvalidVendorTokenException, InvalidVendorCodeException {

        return creditPartnerFactoryService.getPartnerService(loanVendorName).validateExternalRequest(vendorToken,vendorCode);
    }

    @Override
    public LoanOfferDetails createLoanOffer(LoanOfferDetails loanOfferDetails, String vendorToken, String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken,vendorCode,loanOfferDetails.getLoanVendorName());
        return loanOfferService.saveLoanOffer(loanOfferDetails);
    }

    @Override
    public List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId,String vendorToken,String vendorCode,String loanVendorName) throws IOException, InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken,vendorCode,loanVendorName);
        return capitalDataReportService.getCompanySalesDetails(srCompanyId);
    }


    public String getCompanyWiseSalesDetails(String vendorToken,String vendorCode,String loanVendorName) throws Exception {

        validateRequest(vendorToken,vendorCode,loanVendorName);
        List<CompanySalesDetails> companySalesDetailsList = capitalDataReportService.getAllCompanySalesDetails();
        String filePath = "sales_data_"+UUID.randomUUID()+"_salesData.csv";
        String[] headers = generateHeaders(companySalesDetailsList);
        csvUtils.writeCsvWithCustomHeader(companySalesDetailsList,headers,filePath);
        File file =new File(filePath);
        return uploadToS3AndGetPreSignedUri(file,filePath);
    }

    @Override
    public ResponseEntity<?> fetchDocDetailsByTenantId(DocDetailsRequest docDetailsRequest,String vendorToken,String vendorCode,String loanVendorName) throws Exception {
        validateRequest(vendorToken,vendorCode,loanVendorName);
        return docDetailsService.fetchDocDetailsByTenantId(docDetailsRequest);
    }

    private String[] generateHeaders(List<CompanySalesDetails> companySalesDetailsList) {

        String[] data ={"MID", "Vintage on platform (M)","org_type","business_type","" };
        AtomicInteger index =new AtomicInteger(3);
        if(!companySalesDetailsList.isEmpty()){
            companySalesDetailsList.get(0).getDetailsInfo().forEach((k,v)->{
                String key = k.substring(0,k.length()-3).concat("-").concat(k.substring(k.length()-3));
                data[index.incrementAndGet()]= "revenue_"+key+"_collectionAmount";
                data[index.incrementAndGet()]= "revenue_"+key+"_collectionCount";
                data[index.incrementAndGet()]= "revenue_"+key+"_settlementAmount";
            });
        }
        return data;
    }


    private String uploadToS3AndGetPreSignedUri(File file, String  fileName) throws Exception {

        UploadFileToS3Request s3Request = UploadFileToS3Request.builder()
                .bucketName(appProperties.getBucketName())
                .file(file)
                .fileName(fileName)
                .build();
        try {
            S3Util.uploadFileToS3(s3Request.getBucketName(),s3Request.getFileName(),s3Request.getFile());
            Files.deleteIfExists(Path.of(fileName));
        } catch (Exception exception) {
            Files.deleteIfExists(Path.of(fileName));
            log.error("AWS EXCEPTION :: unable to upload document to S3 Bucket. Error: " + exception.getMessage());
            throw new Exception("AWS EXCEPTION");
        }

        GeneratePreSignedUrlRequest preSignedUrlRequest = GeneratePreSignedUrlRequest.builder()
                .filePath(fileName)
                .bucketName(appProperties.getBucketName())
                .httpMethod(HttpMethod.GET)
                .build();

        String preSignedUri = S3Util.generatePreSignedUrl(preSignedUrlRequest);

        return preSignedUri;

    }


}
