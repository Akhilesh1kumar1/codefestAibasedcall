package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class VerifyOtpRequest implements Serializable {

    @NotNull(message = "Verification token is required!")
    @JsonProperty("verification_token")
    private UUID verificationToken;


    @NotBlank(message = "OTP is required!")
    @JsonProperty("otp")
    private String otp;

}
