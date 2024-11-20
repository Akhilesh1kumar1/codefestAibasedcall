package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CreditPartnerConfigRequestDto {

    @NotNull(message = "Account ID cannot be null")
    String accountId;

    @NotNull(message = "Auth code cannot be null")
    String authCode;

    String refreshToken;

    String expiryDateFormat;

    Boolean authCodeHardcoded = false;

    Map<String,String> metaData;

    Long expiryMultiplier=1l;
}
