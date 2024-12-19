package com.sr.capital.config.db;

import com.sr.capital.dto.RequestData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

import java.util.Optional;

@Configuration
@EnableMongoAuditing
public class MongoAuditingConfig {
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable(getCurrentUser());
    }

    private String getCurrentUser() {
        return RequestData.getUserId() != null ? String.valueOf(RequestData.getUserId()) : "System";
    }}