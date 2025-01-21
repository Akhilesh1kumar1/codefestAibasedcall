package com.sr.capital.service;


import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileUploadService {

    String generatePreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);

//    void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId);

    String generateUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, long startTime, HttpMethod method);

    void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto) throws JsonProcessingException, CustomException;

    String downloadAndAddFileToZip(List<KycDocDetails<?>> docDetails) ;


    File downloadFile(String fileName) throws IOException;

    public Boolean deleteFiles(File file);
}
