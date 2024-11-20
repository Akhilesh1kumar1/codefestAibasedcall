package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Checkpoints implements Serializable {

        private String checkpoint;
        private String state;
        private Meta meta;



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
