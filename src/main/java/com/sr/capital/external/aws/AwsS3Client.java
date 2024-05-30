package com.sr.capital.external.aws;

import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sr.capital.config.AppProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AwsS3Client {

    AmazonS3 client = null;

    final AppProperties appConfig;

    @SneakyThrows
    @PostConstruct
    public void getS3SdkInstance() {
        log.info("Creating S3 SDK Instance");

        if (client == null) {
            client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new InstanceProfileCredentialsProvider(true))
                    .withRegion(appConfig.getAwsRegion())
                    .build();
        }
        //S3Util.setS3Client(client);
    }
}
