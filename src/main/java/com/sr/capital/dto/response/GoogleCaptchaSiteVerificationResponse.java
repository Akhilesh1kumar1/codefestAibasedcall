package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GoogleCaptchaSiteVerificationResponse implements Serializable {

    @JsonProperty("success")
    private Boolean success;

    @JsonProperty("challenge_ts")
    private String challengeTime;

    @JsonProperty("hostname")
    private String hostName;

    @JsonProperty("score")
    private Float score;

    @JsonProperty("action")
    private String action;
}
