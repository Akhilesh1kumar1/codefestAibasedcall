package com.sr.capital.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccessTokenResponseDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @NotNull
    String accountId;
    @NotNull
    String accessToken;
    String refreshToken;
    String clientId;
    String clientSecret;
    String expiry;
    String apiKey;
    String apiPassword;
}
