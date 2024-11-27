package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class UserProgressResponseDto {


    String screenName;

    UUID loanId;

    String clientLoanId;

    String comments;

    Long loanVendorId;

    private List<Checkpoint> checkpoints;

    @Builder.Default
    private Boolean showErrorOnPersonalDetails =false;

    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class Checkpoint {
        private String checkpoint;
        private String state;
        private Meta meta;

    }

    @lombok.Data
    @FieldDefaults(level = AccessLevel.PRIVATE)
    @JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    public static class Meta {
        private String subCode;
        private String code;
        private List<String> fields;
        private String message;
    }
}
