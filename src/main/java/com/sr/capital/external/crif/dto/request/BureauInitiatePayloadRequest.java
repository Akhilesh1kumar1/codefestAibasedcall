package com.sr.capital.external.crif.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class BureauInitiatePayloadRequest {

    @NotNull(message = "firstName can not be null")
    String firstName;
    String middleName;
    @NotNull(message = "lastName can not be null")
    String lastName;
    String gender;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    String DOB;
    Integer ageAsOnToday;
    String maritalStatus;
    @NotNull(message = "phone1 can not be null")
    String mobile;
    Integer mob2;
    Integer mob3;
    String email;
    String email2;

    String pan;
    String dl;
    String voterId;
    String passport;
    String rationCard;
    String UID;
    String OtherId1;
    String OtherId2;
    String fatherName;
    String motherName;
    String spouseName;
    String address1;
    String village1;
    String city1;
    String state1;

    String pin1;
    String country1;

    String address2;
    String village2;
    String city2;
    String state112;
    String pin2;
    String country2;
    String customerId;
    String productId;
    String consent;
    String nrega;
    String ckyc;
}
