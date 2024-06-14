package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserTokenSwitchResponse {

    @JsonProperty("status")
    private Boolean status;

    @JsonProperty("response")
    private TokenDetails tokenDetails;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenDetails {

        @JsonProperty("id")
        private String srUserId;

        @JsonProperty("first_name")
        private String firstName;

        @JsonProperty("last_name")
        private String lastName;

        @JsonProperty("email")
        private String email;

        @JsonProperty("company_id")
        private Long companyId;

        @JsonProperty("token")
        private String token;
    }
}
