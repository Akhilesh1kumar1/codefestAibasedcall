package com.sr.capital.service.impl;

import com.omunify.core.util.ExceptionUtils;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.primary.FileUploadData;
import com.sr.capital.repository.mongo.FileUploadDataRepository;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.RedisUtil;
import com.sr.capital.validation.FileValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.omunify.core.util.Constants.GlobalErrorEnum.BAD_REQUEST;
import static com.omunify.core.util.Constants.GlobalErrorEnum.INTERNAL_SERVER_ERROR;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.FILE_IN_PROGRESS_ERROR;
import static com.sr.capital.helpers.constants.Constants.Separators.QUERY_PARAM_SEPARATOR;
import static com.sr.capital.helpers.enums.FileProcessingStatus.ACKNOWLEDGEMENT_PENDING;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {

    final FileUploadDataRepository fileUploadDataRepository;
    final RedisUtil redisUtil;

    LoggerUtil loggerUtil =LoggerUtil.getLogger(FileUploadServiceImpl.class);
    @Override
    public String generatePreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId) {
        String preSignedUrl = "";
        try {
            long startTime = System.currentTimeMillis();
            FileValidator.validateFileUploadRequest(fileUploadRequestDto);
            if (!redisUtil.checkIfFileExists(tenantId)) {
                ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), FILE_IN_PROGRESS_ERROR, HttpStatus.BAD_REQUEST);
            }
            redisUtil.updateFileInCache(tenantId, fileUploadRequestDto.getFileName());
            preSignedUrl = generatePreSignedUrl(fileUploadRequestDto, tenantId, startTime);
            saveFileUploadData(fileUploadRequestDto, tenantId, userId, preSignedUrl, RequestData.getCorrelationId(), startTime);
        } catch (Exception ex) {
            loggerUtil.error("Exception: "+ ex.getMessage()+" occurred while generating pre-signed url for file: "+fileUploadRequestDto.getFileName()+" and tenant ID: {}"+tenantId);
            ExceptionUtils.throwCustomExceptionWithTrace(INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
        return preSignedUrl;
    }

    @Override
    public void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId) {

    }


    private void saveFileUploadData(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId,
                                    String preSignedUrl, String correlationId, long startTime) {
        FileUploadData fileUploadData = FileUploadData.builder()
                .fileName(fileUploadRequestDto.getFileName())
                .correlationId(correlationId)
                .status(ACKNOWLEDGEMENT_PENDING)
                .sourceFileUrl(StringUtils.substringBetween(preSignedUrl, ".com", QUERY_PARAM_SEPARATOR))
                .tenantId(tenantId)
                .uploadedBy(userId)
                .createdAt(Instant.ofEpochMilli(startTime).atZone(ZoneOffset.UTC).toLocalDateTime())
                .build();
        fileUploadDataRepository.save(fileUploadData);
    }



    private void addFileToZip(ZipOutputStream zipOutputStream, String filePath, String entryName) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOutputStream.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, bytesRead);
            }

            zipOutputStream.closeEntry();
        }
    }

}
