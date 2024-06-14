package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiTokenUserDetailsResponse implements Serializable {

    @JsonProperty("data")
    private UserDetails userDetails;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserDetails implements Serializable {

        @JsonProperty("user_id")
        private String userId;

        @JsonProperty("main_user")
        private Boolean mainUser;

        @JsonProperty("full_name")
        private String fullName;

        @JsonProperty("email")
        private String email;

        @JsonProperty("mobile_no")
        private String mobileNumber;

        @JsonProperty("company_id")
        private Long companyId;

        @JsonProperty("company_name")
        private String companyName;
    }
}
