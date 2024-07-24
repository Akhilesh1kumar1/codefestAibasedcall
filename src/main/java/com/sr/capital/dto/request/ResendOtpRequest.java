package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.helpers.enums.CommunicationChannels;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

@Data
public class ResendOtpRequest implements Serializable {

    @NotNull(message = "Verification token is required!")
    @JsonProperty("verification_token")
    private UUID verificationToken;

    @JsonProperty("mobile")
//    @Pattern(regexp = CoreConstants.MOBILE_REGEX, message = ExceptionsDetails.INVALID_MOBILE)
    private String mobile;

    @JsonProperty("channel")
    private CommunicationChannels channel = CommunicationChannels.SMS;
}