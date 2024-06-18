package com.sr.capital.external.shiprocket.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternalTokenUserDetailsResponse implements Serializable {

    @JsonProperty("id")
    private String userId;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("mobile")
    private String mobile;

    @JsonProperty("company_id")
    private String companyId;

    @JsonProperty("company_name")
    private String companyName;


    @JsonProperty("middle_name")
    private String middleName;

    private String comments;

    @JsonProperty("is_accepted")
    private Boolean isAccepted = false;

}
