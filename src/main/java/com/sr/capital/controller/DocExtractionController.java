package com.sr.capital.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.dto.request.UploadExtractAndSaveDocRequest;
import com.sr.capital.kyc.dto.request.VerifyGstOtpRequest;
import com.sr.capital.kyc.service.DocExtractionService;
import com.sr.capital.kyc.service.strategy.ResponseConstructorStrategy;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;


@RestController
@RequestMapping("/api/v1/kyc/doc")
@Validated
public class DocExtractionController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DocExtractionService docExtractionService;

    @Autowired
    private ResponseConstructorStrategy responseConstructorStrategy;

    //REFACTORED & REVISITED
    @PostMapping(value = "/upload_and_extract")
    public ResponseEntity<?> uploadAndExtractDetails(
            @RequestPart(name = "detailString") String detailString ,
            @RequestParam(name = "file1",required = false) MultipartFile file1,
            @RequestParam(name = "file2",required = false) MultipartFile file2) throws Exception {

        DocOrchestratorRequest orchestratorRequest = MapperUtils.readValue(detailString, DocOrchestratorRequest.class);
        RequestData.setRequestType(RequestType.UPLOAD_AND_EXTRACT);

        orchestratorRequest.setFile1(FileDetails.builder().file(file1).build());
        orchestratorRequest.setFile2(FileDetails.builder().file(file2).build());
        orchestratorRequest.setSrCompanyId(RequestData.getTenantId());
        docExtractionService.uploadAndExtractDetails(orchestratorRequest);

        return responseConstructorStrategy.constructResponse(orchestratorRequest);

    }


    @PostMapping(value = "/upload-extract-save")
    public ResponseEntity<?> uploadExtractSave(
             @RequestPart(name = "detailString") String detailString,
            @RequestParam (required = false)MultipartFile file1,
            @RequestParam(required = false) MultipartFile file2) throws Exception {

        UploadExtractAndSaveDocRequest request = MapperUtils.readValue(detailString, UploadExtractAndSaveDocRequest.class);

        DocOrchestratorRequest orchestratorRequest = DocOrchestratorRequest.builder()
                .srCompanyId(request.getSrCompanyId())
                .docType(request.getDocType())
                .actions(request.getActions())
                .file1(FileDetails.builder().file(file1).build())
                .file2(FileDetails.builder().file(file2).build())
                .docDetails(request.getDocDetails()).kycType(request.getKycType())
                .build();
        RequestData.setRequestType(RequestType.UPLOAD_EXTRACT_SAVE);
        docExtractionService.uploadExtractAndSaveDetails(orchestratorRequest);

        return responseConstructorStrategy.constructResponse(orchestratorRequest);

    }

    @PostMapping(value = "/verify-otp", consumes = "application/json", produces = "application/json")
    public GenericResponse verifyGstOtp(@RequestBody DocOrchestratorRequest orchestratorRequest) throws CustomException {
        return docExtractionService.verifyGstOtp(orchestratorRequest);
    }

}
