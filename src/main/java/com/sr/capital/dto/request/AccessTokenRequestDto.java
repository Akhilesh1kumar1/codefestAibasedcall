package com.sr.capital.dto.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenRequestDto {

    @NotNull
    String accountId;
    @NotNull
    String channel;
    String authCode;
    String refreshToken;
    boolean hardRefresh;
    Map<String,String> metaData;

    public AccessTokenRequestDto(String accountId, String channel, boolean hardRefresh) {
        this.accountId = accountId;
        this.channel = channel;
        this.hardRefresh = hardRefresh;
    }
}
