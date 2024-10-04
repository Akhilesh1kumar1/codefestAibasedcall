package com.sr.capital.external.flexi.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LoanDetails {

    private Boolean success;
    private String event;
    private String message;
    private String uuid;
    private Data data;


    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Data {
        private String loanCode;
        private String leadCode;
        private String updatedAt;
        private String s1;
        private String s2;
        private String s3;
        private String applicationStatus;
        private List<Checkpoint> checkpoints;
        private String createdAt;

    }

    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Checkpoint {
        private String checkpoint;
        private String state;
        private Meta meta;

    }

    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Meta {
        private String subCode;
        private String code;
        private List<String> fields;
        private String message;

    }
}
