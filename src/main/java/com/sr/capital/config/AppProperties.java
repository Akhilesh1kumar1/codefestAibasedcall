package com.sr.capital.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class AppProperties {

    //Redis Properties
    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.database}")
    private Integer redisDatabase;

    @Value("${spring.redis.username}")
    private String redisUserName;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.timeout}")
    private String redisTimeout;

    @Value("${spring.redis.useSsl}")
    private Boolean redisUseSsl;

    //aws
    @Value("${aws.region}")
    String awsRegion;

    @Value("${aws.s3.bucketName}")
    String bucketName;


}
