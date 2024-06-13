package com.sr.capital.kyc.dto.request;

import lombok.Builder;
import lombok.Data;

import java.io.File;

@Data
@Builder
public class UploadFileToS3Request {

    private String bucketName;

    private String fileName;

    private File file;
}
