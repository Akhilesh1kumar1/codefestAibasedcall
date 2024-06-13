package com.sr.capital.kyc.dto.request;

import com.amazonaws.HttpMethod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneratePreSignedUrlRequest {

    private String filePath;

    private String bucketName;

    private HttpMethod httpMethod;

    @Builder.Default
    private Integer expiry = 10; //in minutes

    private String objectKey;

    private String fileName;
}
