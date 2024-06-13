package com.sr.capital.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import com.sr.capital.dto.RequestData;
import com.sr.capital.helpers.enums.DocType;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.dto.request.FileDetails;
import com.sr.capital.kyc.dto.request.UploadExtractAndSaveDocRequest;
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
            @RequestParam(name = "doc_type") DocType docType,
            @RequestPart MultipartFile file1,
            @RequestPart(required = false) MultipartFile file2) throws Exception {

        DocOrchestratorRequest orchestratorRequest = DocOrchestratorRequest.builder()
                .srCompanyId(RequestData.getTenantId())
                .docType(docType)
                .file1(FileDetails.builder().file(file1).build())
                .file2(FileDetails.builder().file(file2).build())
                .build();

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
}
