package com.sr.capital.external.crif.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CrifVerifyOtpRequestModels {

    @NotNull(message = "firstName can not be empty")
    @JsonProperty("first_name")
    String firstName;
    @NotNull(message = "lastName can not be empty")
    @JsonProperty("last_name")
    String lastName;
    @NotNull(message = "mobile can not be empty")
    String mobile;
    @NotNull(message = "email can not be empty")
    String email;
    @JsonProperty("doc_type")
    String docType;
    @JsonProperty("doc_value")
    String docValue;

    @NotNull(message = "Verification token is required!")
    @JsonProperty("verification_token")
    private UUID verificationToken;


    @NotBlank(message = "OTP is required!")
    @JsonProperty("otp")
    private String otp;
    
}