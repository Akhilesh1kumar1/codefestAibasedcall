package com.sr.capital.kyc.service;

import com.amazonaws.HttpMethod;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.entity.mongo.kyc.child.PersonalAddressDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.helpers.constants.DocExtractionConstants;
import com.sr.capital.helpers.enums.DocActionType;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.kyc.dto.request.*;
import com.sr.capital.kyc.external.adaptor.KarzaExtractionAdapter;
import com.sr.capital.kyc.external.request.KarzaBaseRequest;
import com.sr.capital.kyc.external.request.VerifyGstExtractionRequest;
import com.sr.capital.kyc.external.request.extraction.data.AadhaarExtractionData;
import com.sr.capital.kyc.external.request.extraction.data.DocumentExtractionData;
import com.sr.capital.kyc.external.response.KarzaBaseResponse;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.kyc.service.strategy.EntityConstructorStrategy;
import com.sr.capital.kyc.service.strategy.ExternalRequestTransformerStrategy;
import com.sr.capital.service.entityimpl.TaskManager;
import com.sr.capital.service.impl.LoanStatusUpdateHandlerServiceImpl;
import com.sr.capital.util.CsvUtils;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.S3Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.http.HttpStatusCode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@Service
public class DocExtractionService {

    @Autowired
    private AppProperties kycAppProperties;

    @Autowired
    private ValidationService validationService;


    @Autowired
    private ExternalRequestTransformerStrategy externalRequestTransformerStrategy;

    @Autowired
    private KarzaExtractionAdapter karzaExtractionAdapter;

    @Autowired
    private EntityConstructorStrategy entityConstructorStrategy;

    @Autowired
    private KycDocDetailsManager kycDocDetailsManager;

    @Autowired
    private TaskManager taskManager;

    @Autowired
    private LoanStatusUpdateHandlerServiceImpl loanStatusUpdateHandlerService;

    private final LoggerUtil loggerUtil = LoggerUtil.getLogger(DocExtractionService.class);

    public void uploadAndExtractDetails(DocOrchestratorRequest orchestratorRequest) throws Exception {

        validationService.validateUploadAndExtractDocRequest(orchestratorRequest);
        if(orchestratorRequest.getIsFileRequired()) {
            generateAndSetFileNames(orchestratorRequest);

            FileDetails file1 = orchestratorRequest.getFile1();
            FileDetails file2 = orchestratorRequest.getFile2();

            uploadToS3AndGetPreSignedUri(file1, Boolean.FALSE);

            if (orchestratorRequest.hasFile2()) {
                uploadToS3AndGetPreSignedUri(file2, Boolean.FALSE);
            }

            transformPdfRequest(orchestratorRequest);

        }
        if(orchestratorRequest.getActions().contains(DocActionType.EXTRACT)){
            KarzaBaseRequest<?> karzaBaseRequest = externalRequestTransformerStrategy.transformExtractionRequest(orchestratorRequest);
            orchestratorRequest.setKarzaBaseRequest(karzaBaseRequest);


            KarzaBaseResponse<?> extractionResponse = karzaExtractionAdapter.extractDocumentDetails(karzaBaseRequest);
            orchestratorRequest.setKarzaBaseResponse(extractionResponse);
        }


        KycDocDetails<?> kycDocDetails = entityConstructorStrategy.constructEntity(orchestratorRequest, orchestratorRequest.getKycDocDetails(),
                getResponseClass(orchestratorRequest.getDocType()));

        loggerUtil.info("[uploadAndExtractDetails] kycDocDetails doc type "+kycDocDetails.getDocType());
        kycDocDetailsManager.saveKycDocDetails(kycDocDetails);

        if(orchestratorRequest.getTask()!=null){
            taskManager.saveTask(orchestratorRequest.getTask());
        }
        orchestratorRequest.setKycDocDetails(kycDocDetails);

        if(orchestratorRequest.getLoanId()!=null) {
            loanStatusUpdateHandlerService.updateLoanState(orchestratorRequest.getLoanId(), orchestratorRequest.getDocType());
        }

    }


    public void uploadExtractAndSaveDetails(DocOrchestratorRequest orchestratorRequest) throws Exception {

        List<DocActionType> docActionTypes = orchestratorRequest.getActions();

        validationService.validateUploadExtractAndSaveDocRequest(orchestratorRequest);

        generateAndSetFileNames(orchestratorRequest);

        FileDetails file1 = orchestratorRequest.getFile1();
        FileDetails file2 = orchestratorRequest.getFile2();

        if(docActionTypes.contains(DocActionType.UPLOAD)){
            uploadToS3AndGetPreSignedUri(file1, Boolean.FALSE);

            if (orchestratorRequest.hasFile2()){
                uploadToS3AndGetPreSignedUri(file2, Boolean.FALSE);
            }
        }


        if(docActionTypes.contains(DocActionType.EXTRACT)){

            KarzaBaseRequest<?> karzaBaseRequest = externalRequestTransformerStrategy.transformExtractionRequest(orchestratorRequest);
            orchestratorRequest.setKarzaBaseRequest(karzaBaseRequest);

            transformPdfRequest(orchestratorRequest);

            KarzaBaseResponse<?> extractionResponse = karzaExtractionAdapter.extractDocumentDetails(karzaBaseRequest);
            orchestratorRequest.setKarzaBaseResponse(extractionResponse);

            KycDocDetails<?> kycDocDetails = entityConstructorStrategy.constructEntity(orchestratorRequest, orchestratorRequest.getKycDocDetails(),
                    getResponseClass(orchestratorRequest.getDocType()));

            orchestratorRequest.setKycDocDetails(kycDocDetails);

        }

        if(docActionTypes.contains(DocActionType.SAVE)){
            KycDocDetails<?> kycDocDetails = entityConstructorStrategy.constructEntity(orchestratorRequest, orchestratorRequest.getKycDocDetails(),
                    getResponseClass(orchestratorRequest.getDocType()));

            orchestratorRequest.setKycDocDetails(kycDocDetails);
            kycDocDetailsManager.saveKycDocDetails(kycDocDetails);
        }
    }


    private void uploadToS3AndGetPreSignedUri(FileDetails fileDetails, Boolean isPdf) throws Exception {

        UploadFileToS3Request s3Request = UploadFileToS3Request.builder()
                .bucketName(kycAppProperties.getBucketName())
                .file(isPdf ? CsvUtils.extractAndSaveSinglePageFromPdf(fileDetails, 0) : CsvUtils.convertMultiPartToFile(fileDetails))
                .fileName(fileDetails.getFileName())
                .build();
        try {
           S3Util.uploadFileToS3(s3Request.getBucketName(),s3Request.getFileName(),s3Request.getFile());
            Files.deleteIfExists(Path.of(fileDetails.getFileName()));
        } catch (Exception exception) {
            Files.deleteIfExists(Path.of(fileDetails.getFileName()));
            loggerUtil.error("AWS EXCEPTION :: unable to upload document to S3 Bucket. Error: " + exception.getMessage());
            throw new Exception("AWS EXCEPTION");
        }

        GeneratePreSignedUrlRequest preSignedUrlRequest = GeneratePreSignedUrlRequest.builder()
                .filePath(fileDetails.getFileName())
                .bucketName(kycAppProperties.getBucketName())
                .httpMethod(HttpMethod.GET)
                .build();

        loggerUtil.info("generate presignged url ");
        String preSignedUri = S3Util.generatePreSignedUrl(preSignedUrlRequest);

        fileDetails.setPreSignedUri(preSignedUri);

    }

    private Class<?> getResponseClass(DocType docType) throws CustomException {
        switch (docType){
            /*case AADHAAR:
                return AadhaarDocDetails.class;*/
            case BANK_CHEQUE:
                return BankDocDetails.class;
           /* case GST:
                return GstDocDetails.class;
            case PAN:
                return PanDocDetails.class;*/
            case SELFI:
                return SelfiDocDetails.class;
            case GST_BY_PAN:
                return GstByPanDocDetails.class;
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
                return ReportMetaData.class;
            case BUSINESS_ADDRESS:
                return BusinessAddressDetails.class;
            case PERSONAL_ADDRESS:
                return PersonalAddressDetails.class;
            case ITR:
                 return ItrDocDetails.class;
            default:
                throw new CustomException("Invalid Doc Type!", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    public void generateAndSetFileNames(DocOrchestratorRequest orchestratorRequest) {

        FileDetails file1 = orchestratorRequest.getFile1();
        FileDetails file2 = orchestratorRequest.getFile2();

        List<String> dotSplit = List.of(file1.getFile().getOriginalFilename().split("\\."));
        String extension = dotSplit.get(dotSplit.size()-1);

        file1.setFileId(UUID.randomUUID());
        file1.setFileName(orchestratorRequest.getDocType() + "-" + file1.getFileId() + "." + extension);

        if(orchestratorRequest.hasFile2()) {
            dotSplit = List.of(file2.getFile().getOriginalFilename().split("\\."));
            extension = dotSplit.get(dotSplit.size()-1);

            file2.setFileId(UUID.randomUUID());
            file2.setFileName(orchestratorRequest.getDocType() + "-" + file2.getFileId() + "." + extension);
        }
    }

    private void transformPdfRequest(DocOrchestratorRequest orchestratorRequest) throws Exception {

        KarzaBaseRequest<?> karzaBaseRequest = orchestratorRequest.getKarzaBaseRequest();
        if(karzaBaseRequest==null){
            return;
        }
        FileDetails file1 = orchestratorRequest.getFile1();
        FileDetails file2 = orchestratorRequest.getFile2();

        if (DocExtractionConstants.FileTypes.PDF.equalsIgnoreCase(file1.getFile().getContentType())) {

            FileDetails fileDetails = FileDetails.builder()
                    .file(file1.getFile())
                    .fileName(file1.getFileName())
                    .fileId(file1.getFileId())
                    .build();

            uploadToS3AndGetPreSignedUri(fileDetails, Boolean.TRUE);

            if (karzaBaseRequest.getData() instanceof AadhaarExtractionData) {
                AadhaarExtractionData aadhaarExtractionData = (AadhaarExtractionData) karzaBaseRequest.getData();
                aadhaarExtractionData.setDocument1(fileDetails.getPreSignedUri());

            } else {
                DocumentExtractionData documentExtractionData = (DocumentExtractionData) karzaBaseRequest.getData();
                documentExtractionData.setDocument1(fileDetails.getPreSignedUri());
            }
        }

        if (orchestratorRequest.hasFile2() && DocExtractionConstants.FileTypes.PDF.equalsIgnoreCase(file2.getFile().getContentType())) {

            FileDetails fileDetails = FileDetails.builder()
                    .file(file2.getFile())
                    .fileName(file2.getFileName())
                    .fileId(file2.getFileId())
                    .build();

            uploadToS3AndGetPreSignedUri(fileDetails, Boolean.TRUE);

            if (karzaBaseRequest.getData() instanceof AadhaarExtractionData) {
                AadhaarExtractionData aadhaarExtractionData = (AadhaarExtractionData) karzaBaseRequest.getData();
                aadhaarExtractionData.setDocument1(fileDetails.getPreSignedUri());

            }
        }

    }

    public GenericResponse verifyGstOtp(DocOrchestratorRequest orchestratorRequest) throws RequestTransformerNotFoundException, ServiceEndpointNotFoundException {

        VerifyGstOtpRequest verifyGstOtpRequest = null;
        try {
            verifyGstOtpRequest = MapperUtils.convertValue(orchestratorRequest.getDocDetails(),VerifyGstOtpRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        VerifyGstExtractionRequest karzaBaseRequest = VerifyGstExtractionRequest. builder().data(verifyGstOtpRequest).build();
        orchestratorRequest.setKarzaBaseRequest(karzaBaseRequest);


        KarzaBaseResponse<?> extractionResponse = karzaExtractionAdapter.extractDocumentDetails(karzaBaseRequest);
        orchestratorRequest.setKarzaBaseResponse(extractionResponse);
        GenericResponse response = new GenericResponse<>();
        response.setStatusCode(HttpStatusCode.OK);
        response.setData(extractionResponse);
       return response;
    }


}
