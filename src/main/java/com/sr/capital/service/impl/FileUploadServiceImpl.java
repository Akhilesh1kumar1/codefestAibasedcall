package com.sr.capital.service.impl;

import com.omunify.core.util.ExceptionUtils;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.file.FileUploadRequestDTO;
import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.entity.primary.FileUploadData;
import com.sr.capital.repository.mongo.FileUploadDataRepository;
import com.sr.capital.service.FileUploadService;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.RedisUtil;
import com.sr.capital.util.S3Util;
import com.sr.capital.validation.FileValidator;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
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
    final AppProperties appProperties;

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
                            loggerUtil.info("Processing image: " +docPath);

                            if (io.micrometer.common.util.StringUtils.isNotBlank(docPath)) {
                                try (InputStream inputStream = S3Util.downloadObjectToFile(appProperties.getBucketName(),docPath)) {
                                    if (inputStream != null) {
                                        String fileName = docPath;
                                        String filePath = tempDir.getAbsolutePath() + "/" + fileName;
                                        loggerUtil.info("fileName :- "+ fileName);
                                        loggerUtil.info("filePath Done:"+ filePath);
                                        writeInputStreamToFile(inputStream, filePath);

                                        addFileToZip(zipOutputStream, filePath, fileName);


                                        loggerUtil.info("File "+fileName+" added to the zip");
                                    }
                                } catch (IOException e) {
                                    loggerUtil.error("Error while processing file from AWS: "+ e.getMessage());
                                }
                            }
                        }
                        isAllFileAddedToZip=true;
                    }
                }
            }
            if(isAllFileAddedToZip){
                loggerUtil.info("uploading zip file to s3 ");
                S3Util.uploadFileToS3(appProperties.getBucketName(),zipFilePath,new File(zipFilePath));
            }
        } catch (IOException e) {
            return zipFilePath;
        }finally {
            if (tempDir != null) {
                loggerUtil.info("Inside finally block for clean up processing :- ");
                deleteDirectory(tempDir);
            }
        }



        return zipFilePath;
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

    private void deleteDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    loggerUtil.info("deleting file:- "+ file.getName());
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
            loggerUtil.error("Error writing input stream to file: " + e.getMessage());
            throw e;
        }
    }
}
