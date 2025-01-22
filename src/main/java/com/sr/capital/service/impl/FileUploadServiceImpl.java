package com.sr.capital.service.impl;

import com.amazonaws.HttpMethod;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.omunify.core.util.ExceptionUtils;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.primary.FileUploadData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import com.sr.capital.kyc.service.DocExtractionService;
import com.sr.capital.repository.mongo.FileUploadDataRepository;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.*;
import com.sr.capital.validation.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static com.omunify.core.util.Constants.GlobalErrorEnum.BAD_REQUEST;
import static com.omunify.core.util.Constants.GlobalErrorEnum.INTERNAL_SERVER_ERROR;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.FILE_IN_PROGRESS_ERROR;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.FILE_NAME_ALREADY_EXIST_ERROR;
import static com.sr.capital.helpers.enums.FileProcessingStatus.ACKNOWLEDGEMENT_PENDING;
import static com.sr.capital.helpers.enums.KafkaEventTypes.PROCESS_UPLOAD_DATA_EVENT;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {

    final FileUploadDataRepository fileUploadDataRepository;
    final RedisUtil redisUtil;
    final AppProperties appProperties;
    private final DocExtractionService docExtractionService;
    private final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;

    @Override
    public String generatePreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId, HttpMethod method) {
        String preSignedUrl = "";
        try {
            FileUploadData fileUploadOldData = fileUploadDataRepository.findByTenantIdAndUploadedByAndFileName(tenantId, userId, fileUploadRequestDto.getFileName());
            if (fileUploadOldData != null) {
                ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), FILE_NAME_ALREADY_EXIST_ERROR, HttpStatus.BAD_REQUEST);
            }
            long startTime = System.currentTimeMillis();
            FileValidator.validateFileUploadRequest(fileUploadRequestDto);
            if (!redisUtil.checkIfFileExists(tenantId)) {
                ExceptionUtils.throwCustomException(BAD_REQUEST.getCode(), FILE_IN_PROGRESS_ERROR, HttpStatus.BAD_REQUEST);
            }
            redisUtil.updateFileInCache(tenantId, fileUploadRequestDto.getFileName());
            preSignedUrl = generateUrl(fileUploadRequestDto, tenantId, startTime, method);
            saveFileUploadData(fileUploadRequestDto, tenantId, userId, preSignedUrl, fileUploadRequestDto.getCorrelationId(), startTime);
        } catch (Exception ex) {
            log.error("Exception: "+ ex.getMessage()+" occurred while generating pre-signed url for file: "+fileUploadRequestDto.getFileName()+" and tenant ID: {}"+tenantId);
            ExceptionUtils.throwCustomExceptionWithTrace(INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
        return preSignedUrl;
    }

    @Override
    public String generateDownloadPreSignedUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId, HttpMethod method) {
        String preSignedUrl = "";
        try {
            long startTime = System.currentTimeMillis();
            FileValidator.validateFileUploadRequest(fileUploadRequestDto);
            preSignedUrl = generateUrl(fileUploadRequestDto, tenantId, startTime, method);
        } catch (Exception ex) {
            log.error("Exception: "+ ex.getMessage()+" occurred while generating pre-signed url for file: "+fileUploadRequestDto.getFileName()+" and tenant ID: {}"+tenantId);
            ExceptionUtils.throwCustomExceptionWithTrace(INTERNAL_SERVER_ERROR.getCode(), ex.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR, ex);
        }
        return preSignedUrl;
    }

    private String generateUrl(FileUploadRequestDTO fileUploadRequestDto, String tenantId, long startTime, HttpMethod method) {
        GeneratePreSignedUrlRequest preSignedUrlRequest = GeneratePreSignedUrlRequest.builder()
                .filePath(fileUploadRequestDto.getFileName())
                .bucketName(appProperties.getBucketName())
                .httpMethod(method)
                .build();

        log.info("generate pre-signed url ");
        String preSignedUrl = S3Util.generateUrl(preSignedUrlRequest);
        log.info("pre-signed url " +preSignedUrl);

        return preSignedUrl;
    }

    @Override
    public void acknowledgeFile(FileUploadRequestDTO fileUploadRequestDto) throws JsonProcessingException, CustomException {
        FileUploadData fileUpload = fileUploadDataRepository.findByTenantIdAndCorrelationId(RequestData.getTenantId(), fileUploadRequestDto.getCorrelationId());
        if (fileUpload != null) {
            kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName(), kafkaMessagePublisherUtil.
                    getKafkaMessage(MapperUtils.writeValueAsString(fileUploadRequestDto),
                            PROCESS_UPLOAD_DATA_EVENT.name(), null, fileUploadRequestDto.getCorrelationId(), null));
        } else {
            throw new CustomException("Invalid Data");
        }
    }

    @Override
    public String downloadAndAddFileToZip(List<KycDocDetails<?>> docDetails)  {
        File tempDir = null;
        boolean isAllFileAddedToZip =false;
        String zipFilePath=null;
        try {
            tempDir = createTempDirectory();
             zipFilePath = tempDir.getAbsolutePath() + "/"+docDetails.get(0).getSrCompanyId()+"_doc.zip";
            try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFilePath))) {
                for (KycDocDetails<?> details : docDetails) {

                    if(CollectionUtils.isNotEmpty(details.getImages())) {
                        for (String docPath: details.getImages()) {
                            log.info("Processing image: " +docPath);

                            if (io.micrometer.common.util.StringUtils.isNotBlank(docPath)) {
                                try (InputStream inputStream = S3Util.downloadObjectToFile(appProperties.getBucketName(),docPath)) {
                                    if (inputStream != null) {
                                        String fileName = docPath;
                                        String filePath = tempDir.getAbsolutePath() + "/" + fileName;
                                        log.info("fileName :- "+ fileName);
                                        log.info("filePath Done:"+ filePath);
                                        writeInputStreamToFile(inputStream, filePath);

                                        addFileToZip(zipOutputStream, filePath, fileName);


                                        log.info("File "+fileName+" added to the zip");
                                    }
                                } catch (IOException e) {
                                    log.error("Error while processing file from AWS: "+ e.getMessage());
                                }
                            }
                        }
                        isAllFileAddedToZip=true;
                    }
                }
            }
            if(isAllFileAddedToZip){
                log.info("uploading zip file to s3 ");
                String key = docDetails.get(0).getSrCompanyId()+"_"+ UUID.randomUUID()+"_doc.zip";
                S3Util.uploadFileToS3(appProperties.getBucketName(),key,new File(zipFilePath));
            }
        } catch (IOException e) {
            return zipFilePath;
        }finally {
            if (tempDir != null) {
                log.info("Inside finally block for clean up processing :- ");
                deleteDirectory(tempDir);
            }
        }



        return zipFilePath;
    }

    @Override
    public File downloadFile(String fileName) throws IOException {
        File tempDir = createTempDirectory();
        return S3Util.downloadFileFromS3(appProperties.getBucketName(),fileName,tempDir);
    }

    @Override
    public Boolean deleteFiles(File file) {
         try {
             deleteDirectory(file);

         }catch (Exception ex) {
         }
         return true;
    }


    private String getDocExtension(String filePath) {
        int lastIndexOfDot = filePath.lastIndexOf('.');
        if (lastIndexOfDot > 0 && lastIndexOfDot < filePath.length() - 1) {
            return filePath.substring(lastIndexOfDot + 1);
        } else {
            return "";
        }
    }


    private void saveFileUploadData(FileUploadRequestDTO fileUploadRequestDto, String tenantId, Long userId,
                                    String preSignedUrl, String correlationId, long startTime) {
        FileUploadData fileUploadOldData = fileUploadDataRepository.findByTenantIdAndUploadedByAndFileName(tenantId, userId, fileUploadRequestDto.getFileName());
        FileUploadData fileUploadData = FileUploadData.builder()
                .fileName(fileUploadRequestDto.getFileName())
                .correlationId(correlationId)
                .status(ACKNOWLEDGEMENT_PENDING)
                .tenantId(tenantId)
                .uploadedBy(userId)
                .build();
        if (fileUploadOldData != null) {
            fileUploadData.setId(fileUploadOldData.getId());
        }
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



    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    log.info("deleting file:- "+ file.getName());
                    deleteDirectory(file);
                }
            }
        }
        directory.delete();
    }

    private File createTempDirectory() throws IOException {
        return Files.createTempDirectory("tempDir").toFile();
    }



    private void writeInputStreamToFile(InputStream inputStream, String filePath) throws IOException {
       try (OutputStream outputStream = new FileOutputStream(filePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            log.error("Error writing input stream to file: " + e.getMessage());
            throw e;
        }
    }
}
