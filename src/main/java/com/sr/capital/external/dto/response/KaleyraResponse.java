package com.sr.capital.external.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KaleyraResponse {
    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private List<Response> data;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Response implements Serializable {
        @JsonProperty("id")
        private String id;

        @JsonProperty("customid")
        private String customId;

        @JsonProperty("customid1")
        private String customId1;

        @JsonProperty("customid2")
        private String customId2;

        @JsonProperty("mobile")
        private String mobile;

        @JsonProperty("status")
        private String status;
    }

    @JsonProperty("message")
    private String message;
}
