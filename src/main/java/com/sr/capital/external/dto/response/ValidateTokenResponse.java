package com.sr.capital.external.dto.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ValidateTokenResponse {

    String code;

    String msg;

    ModelDetails model;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ModelDetails{
          Long companyId;
          TokenDetails tokenData;
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenDetails{
        Long sub;
        String source;
        Boolean isAdmin;
        Long adminUserId;
        Long exp;
        String jti;
        Long iat;
    }
}
