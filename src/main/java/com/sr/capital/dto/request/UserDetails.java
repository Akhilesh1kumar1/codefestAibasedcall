package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.primary.User;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDetails {

    @JsonProperty("id")
    private Long id;

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

    private String middleName;

    private String lastName;

    private String firstName;

    private String comments;

    private Boolean isAccepted = false;

    private String entityType;

    private String panNumber;

    private Boolean isMobileNumberVerified;

    private UUID verificationToken;

    private String dateOfBirth;

    public static UserDetails mapUser(User user){
        UserDetails userDetails =new UserDetails();
        userDetails.setId(user.getId());
        return userDetails;
    }

}
