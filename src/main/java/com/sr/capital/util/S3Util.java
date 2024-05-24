package com.sr.capital.util;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.sr.capital.exception.custom.CustomServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;


import static com.sr.capital.helpers.constants.Constants.FILE_CONTENT_TYPE_MAP;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.ERROR_WHILE_READING_DATA_FROM_AWS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.ERROR_WHILE_UPLOADING_FILE_TO_AWS;
import static com.sr.capital.helpers.constants.Constants.ServiceConstants.UTILITY_INSTANTIATION_ERROR;

@Slf4j
public class S3Util {

    private S3Util() {
        throw new AssertionError(UTILITY_INSTANTIATION_ERROR);
    }

    static AmazonS3 s3Client;

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

}