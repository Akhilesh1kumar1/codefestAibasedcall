package com.sr.capital.config;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RedisConfig {

    @Value("${redis.config}")
    String file;

    @Bean
    public RedissonClient redissonClient() throws IOException {
        return Redisson.create(Config.fromYAML(new ClassPathResource(file).getInputStream()));
    }
}
