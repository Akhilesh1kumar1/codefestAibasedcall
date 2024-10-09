package com.sr.capital.service.impl;

import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.LoanStatusUpdateWebhookDto;
import com.sr.capital.dto.response.CompanySalesDetails;
import com.sr.capital.dto.response.LoanOfferDetails;
import com.sr.capital.dto.response.MonthlySalesDetails;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.entity.primary.User;
import com.sr.capital.entity.primary.WhatsappApiLog;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.dto.request.KaleyraWebhookDto;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.kyc.dto.request.DocDetailsRequest;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.dto.request.UploadFileToS3Request;
import com.sr.capital.kyc.service.DocDetailsService;
import com.sr.capital.service.*;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import com.sr.capital.service.entityimpl.WhatsAppEntityServiceImpl;
import com.sr.capital.util.CsvUtils;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.temporal.ChronoUnit;
import java.util.*;

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
    final BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;
    final WhatsAppEntityServiceImpl whatsAppEntityService;
    final LeadGenerationService leadGenerationService;
    final CommunicationService communicationService;
    final UserService userService;

    final LoanStatusUpdateHandlerServiceImpl loanStatusUpdateHandlerService;

    @Override
    public Boolean validateRequest(String vendorToken, String vendorCode, String loanVendorName) throws InvalidVendorTokenException, InvalidVendorCodeException {

        return creditPartnerFactoryService.getPartnerService(loanVendorName).validateExternalRequest(vendorToken, vendorCode);
    }

    @Override
    public LoanOfferDetails createLoanOffer(LoanOfferDetails loanOfferDetails, String vendorToken, String vendorCode) throws InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken, vendorCode, loanOfferDetails.getLoanVendorName());
        BaseCreditPartner baseCreditPartnerResponseDto = baseCreditPartnerEntityService.getCreditPartnerByName(loanOfferDetails.getLoanVendorName());
        if (baseCreditPartnerResponseDto != null) {
            loanOfferDetails.setLoanVendorId(baseCreditPartnerResponseDto.getId());
        }
        return loanOfferService.saveLoanOffer(loanOfferDetails);
    }

    @Override
    public List<CompanySalesDetails> getCompanySalesDetails(Long srCompanyId, String vendorToken, String vendorCode, String loanVendorName) throws IOException, InvalidVendorTokenException, InvalidVendorCodeException {
        validateRequest(vendorToken, vendorCode, loanVendorName);
        return capitalDataReportService.getCompanySalesDetails(srCompanyId);
    }


    public String getCompanyWiseSalesDetails(String vendorToken, String vendorCode, String loanVendorName) throws Exception {

        validateRequest(vendorToken, vendorCode, loanVendorName);
        List<CompanySalesDetails> companySalesDetailsList = capitalDataReportService.getAllCompanySalesDetails();
        String filePath = "sales_data_" + UUID.randomUUID() + "_salesData.csv";
        List<String> monthKeys = CsvUtils.generateDateKeys(13, "yyyyMM", ChronoUnit.MONTHS);
        monthKeys.remove(0);
        Collections.reverse(monthKeys);
        String[] headers = generateSalesDetailsHeaders(monthKeys);
        List<String[]> data = generateSalesDetailsData(companySalesDetailsList, monthKeys);
        csvUtils.writeCsvWithCustomHeader(data, headers, filePath);
        File file = new File(filePath);
        return uploadToS3AndGetPreSignedUri(file, filePath);
    }

    @Override
    public ResponseEntity<?> fetchDocDetailsByTenantId(DocDetailsRequest docDetailsRequest, String vendorToken, String vendorCode, String loanVendorName) throws Exception {
        validateRequest(vendorToken, vendorCode, loanVendorName);
        return docDetailsService.fetchDocDetailsByTenantId(docDetailsRequest);
    }

    @Override
    public ResponseEntity<?> saveWhatsAppCommunication(KaleyraWebhookDto content) {

        log.info("[saveWhatsAppCommunication] webhook content : {} ", content);

        if (content != null && content.getReplyTo() != null) {
            WhatsappApiLog whatsappApiLog = whatsAppEntityService.getApiLog(content.getReplyTo());

            if (whatsappApiLog != null) {
                leadGenerationService.updateRemarks(whatsappApiLog.getInternalId(), content.getBody());
            }

            if (whatsappApiLog.getEventType().equalsIgnoreCase("lead")) {
                User user = userService.getCompanyDetails(whatsappApiLog.getSrCompanyId());
                if (user != null) {
                    communicationService.sendCommunication(communicationService.getCommunicationRequestForSellerNotConnectedViadWhatsApp(user.getMobile(), new ArrayList<>(), appProperties.getKaleyraWhatsappSellerThanksTemplateName()));
                }
            }
        }


        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> saveLoanStatus(String vendorToken,String vendorCode,String loanVendorName,Map<String, Object> loanStatusWebhook) throws InvalidVendorTokenException, InvalidVendorCodeException, IOException {

        log.info("[saveLoanStatus] webhook content : {} ", loanStatusWebhook);

        creditPartnerFactoryService.getPartnerService(loanVendorName).validateExternalRequest(vendorToken, vendorCode);
        LoanStatusUpdateWebhookDto loanStatusUpdateWebhookDto = MapperUtils.convertValue(loanStatusWebhook, LoanStatusUpdateWebhookDto.class);
        loanStatusUpdateHandlerService.handleStatusUpdate(loanStatusUpdateWebhookDto,loanVendorName);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);

    }

    private String[] generateSalesDetailsHeaders(List<String> monthKeys) {

        List<String> data = new ArrayList<>(
                Arrays.asList("MID", "Vintage on platform (M)", "org_type", "business_type", "GST (Yes/No)")
        );
        monthKeys.forEach((key) -> {
            String newKey = key.substring(0, key.length() - 2).concat("-")
                    .concat(key.substring(key.length() - 2));
            data.add("revenue_" + newKey + "_collectionAmount");
            data.add("revenue_" + newKey + "_collectionCount");
            data.add("revenue_" + newKey + "_settlementAmount");
        });
        return data.toArray(new String[0]);
    }

    private List<String[]> generateSalesDetailsData(List<CompanySalesDetails> companySalesDetailsList, List<String> monthKeys) {
        List<String[]> data = new ArrayList<>();

        for (CompanySalesDetails companySalesDetails : companySalesDetailsList) {
            List<String> line = new ArrayList<>(
                    Arrays.asList(
                            String.valueOf(companySalesDetails.getSrCompanyId()),
                            String.valueOf(companySalesDetails.getAgeInSr()),
                            companySalesDetails.getOrgKyc(),
                            "", ""
                    )
            );
            monthKeys.forEach((key) -> {
                Optional<MonthlySalesDetails> salesDetails = Optional.ofNullable(companySalesDetails.getDetailsInfo().get(key));
                line.add(salesDetails.map(monthlySalesDetails -> monthlySalesDetails.getMonthCodGmv().toString()).orElse(""));
                line.add(salesDetails.map(monthlySalesDetails -> monthlySalesDetails.getMonthShipments().toString()).orElse(""));
                line.add(salesDetails.map(monthlySalesDetails -> monthlySalesDetails.getMonthRemittedValue().toString()).orElse(""));
            });
            data.add(line.toArray(new String[0]));
        }

        return data;
    }


    private String uploadToS3AndGetPreSignedUri(File file, String fileName) throws Exception {

        UploadFileToS3Request s3Request = UploadFileToS3Request.builder()
                .bucketName(appProperties.getBucketName())
                .file(file)
                .fileName(fileName)
                .build();
        try {
            S3Util.uploadFileToS3(s3Request.getBucketName(), s3Request.getFileName(), s3Request.getFile());
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
