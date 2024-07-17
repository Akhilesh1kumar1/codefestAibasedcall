package com.sr.capital.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3URI;
import com.amazonaws.services.s3.model.*;
import com.omunify.core.util.ExceptionUtils;
import com.sr.capital.dto.request.BankDetailsRequestDto;
import com.sr.capital.exception.custom.CustomServiceException;
import com.sr.capital.kyc.dto.request.GeneratePreSignedUrlRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import org.apache.commons.io.IOUtils;
import java.net.URL;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;


import static com.sr.capital.helpers.constants.Constants.FILE_CONTENT_TYPE_MAP;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.ERROR_WHILE_READING_DATA_FROM_AWS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.ERROR_WHILE_UPLOADING_FILE_TO_AWS;
import static com.sr.capital.helpers.constants.Constants.Separators.EXTENSION_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.Separators.SLASH_SEPARATOR;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.BANK_IMAGE_FOLDER_NAME;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;

@Slf4j
public class S3Util {

    private S3Util() {
        throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
    }

    static AmazonS3 s3Client;
    static final String CAPITAL_FOLDER = "capital";

    public static void setS3Client(AmazonS3 s3client) {
        S3Util.s3Client = s3client;
    }

    public static String getUpdatingPreSignedUrl(String bucketName, String objectKey, long expiryTime, String fileName) {
        log.info("Generating pre-signed URL from S3 for file: {} with object key: {}", fileName, objectKey);
        try {
            Date expiration = new Date();
            long expTimeMillis = expiration.getTime();
            expTimeMillis += expiryTime;
            expiration.setTime(expTimeMillis);

            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectKey)
                            .withMethod(HttpMethod.PUT)
                            .withExpiration(expiration)
                            .withContentType(FILE_CONTENT_TYPE_MAP.get(FilenameUtils.getExtension(fileName)));

            URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
            return presignedUrl.toString();
        } catch (AmazonS3Exception e) {
            log.error("AWS S3 exception while generating pre-signed url: ObjectKey {}, message {}", objectKey, e.getMessage());
            throw new CustomServiceException("Something went wrong while generating S3 pre-signed url. Please try later.");
        }
    }

    public static S3Object getObjectFromS3(String bucketName, String key) {
        log.info("Fetching file from S3 from bucket name: {}, for key: {}", bucketName, key);
        try {
            return s3Client.getObject(bucketName, key);
        } catch (AmazonS3Exception ex) {
            throw new CustomServiceException(String.format(ERROR_WHILE_READING_DATA_FROM_AWS, key, ex.getMessage()));
        }
    }


    public static String uploadObjectTos3(byte[] csv, String tenantId, String fileName, String bucketName) {
        log.info("Uploading file: {} to S3 for tenant: {}", fileName, tenantId);
        try {
            //TODO
            String key = null;
            s3Client.putObject(bucketName, key, new ByteArrayInputStream(csv), new ObjectMetadata());
            return key;
        } catch (AmazonS3Exception ex) {
            throw new CustomServiceException(String.format(ERROR_WHILE_UPLOADING_FILE_TO_AWS, fileName, ex.getMessage()));
        }
    }

    public static void uploadObjectTos3WithKey(byte[] csv, String tenantId,String bucketName, String key) {
        log.info("Uploading file: {} to S3 for tenant: {}", key, tenantId);
        try {
            s3Client.putObject(bucketName, key, new ByteArrayInputStream(csv), new ObjectMetadata());
        } catch (AmazonS3Exception ex) {
            throw new CustomServiceException(String.format(ERROR_WHILE_UPLOADING_FILE_TO_AWS, key, ex.getMessage()));
        }
    }

    public static String getFolderString(String... var) {
        return CAPITAL_FOLDER +
                SLASH_SEPARATOR +
                Arrays.stream(var).reduce((x, y) -> x + SLASH_SEPARATOR + y).orElse(null);
    }

    public static void uploadFileToS3(String bucketName, String s3Key, File file) {
        try {
            s3Client.putObject(new PutObjectRequest(bucketName, s3Key, file).withCannedAcl(CannedAccessControlList.BucketOwnerRead));
        } catch (AmazonS3Exception ex) {
            log.error("Error while uploading the file to S3 : {}, error : {}", file.getName(), ex.getMessage());
            throw ExceptionUtils.customException(HttpStatus.INTERNAL_SERVER_ERROR.name(),
                    String.format("Error while file upload :%s , error : %s", file.getName(), ex.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static String uploadDocument(MultipartFile multipartFile, String bucketName,String prefixId,String folderName,String entityId,AtomicInteger imageNumber){

        String fileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        String imageFolder = S3Util.getFolderString(String.valueOf(prefixId), folderName, entityId);
        String imageFileKeyName = imageFolder + SLASH_SEPARATOR + imageNumber.getAndIncrement() +
                EXTENSION_SEPARATOR + fileExtension;

        File file = CsvUtils.convertMultipartFileToFile(multipartFile, imageFolder, imageFileKeyName);
        S3Util.uploadFileToS3(bucketName, imageFileKeyName, file);
        String imageLink = imageFileKeyName;

        CsvUtils.deleteFile(imageFolder, file);
        return imageLink;
    }

    public static String generatePreSignedUrl(GeneratePreSignedUrlRequest request) {

        log.info("[generatePreSignedUrl]for request {} ",request);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, request.getExpiry());
        
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(request.getBucketName(), request.getFilePath())
                        .withMethod(request.getHttpMethod())
                        .withExpiration(calendar.getTime());
                        //.withContentType(FILE_CONTENT_TYPE_MAP.get(FilenameUtils.getExtension(request.getFileName())));

        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        log.info("[generatePreSignedUrl] url {}",presignedUrl.toString());
        return presignedUrl.toString();
    }


    public static InputStream downloadObjectToFile(String bucketName,String entityKey) {
        S3Object s3Object = null;
        S3ObjectInputStream s3ObjectInputStream;
        InputStream is;
        try {
            s3Object = s3Client.getObject(bucketName, entityKey);
            s3ObjectInputStream = s3Object.getObjectContent();
            ByteArrayOutputStream temp = new ByteArrayOutputStream();
            IOUtils.copy(s3ObjectInputStream, temp);
            is = new ByteArrayInputStream(temp.toByteArray());
        } catch (AmazonS3Exception e) {
            log.error("AWS S3 exception:" + e.getMessage());
            throw new CustomServiceException("Something went wrong while downloading file.Please try later.");
        } catch (Exception e) {
            log.error("Excpetion while downloading file:" + e.getMessage());
            throw new CustomServiceException("Something went wrong while downloading file.Please try later.");
        } finally {
            try {
                if (s3Object != null) {
                    s3Object.close();
                }
            } catch (IOException e) {
                log.error("Error while closing s3Object : {}", e.getMessage());
            }
        }
        return is;
    }
}