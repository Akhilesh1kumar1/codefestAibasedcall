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

    @JsonProperty("entity_type")
    private String entityType;

    @JsonProperty("pan_number")
    private String panNumber;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("father_name")
    private String fatherName;


    private String gender;

   private Boolean isMobileVerified=true;

    @JsonProperty("current_account_available")
    private Boolean currentAccountAvailable;

}
