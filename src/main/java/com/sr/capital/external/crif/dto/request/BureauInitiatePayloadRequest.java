package com.sr.capital.external.crif.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BureauInitiatePayloadRequest {

    @NotNull(message = "firstName can not be null")
    String firstName;
    String middleName;
    @NotNull(message = "lastName can not be null")
    String lastName;
    String gender;
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    @NotNull(message = "DOB can not be null")
    String DOB;
    Integer ageAsOnToday;
    String maritalStatus;
    @NotNull(message = "phone1 can not be null")
    String mob1;
    Integer mob2;
    Integer mob3;
    @NotNull(message = " can not be null")
    String email1;
    String email2;

    // TODO :: Need to create custom Annotation to mark any of field is reqiured
    @NotNull(message = "pan can not be null")
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
    @NotNull(message = "address1 can not be null")
    String address1;
    @NotNull(message = "village1can not be null")
    String village1;
    @NotNull(message = "city1 can not be null")
    String city1;
    @NotNull(message = "state11 can not be null")
    String state1;

    @NotNull(message = "pin1 can not be null")
    String pin1;
    @NotNull(message = "country1 can not be null")
    String country1;

    String address2;
    String village2;
    String city2;
    String state112;
    String pin2;
    String country2;
    @NotNull(message = "customerId can not be null")
    String customerId;
    @NotNull(message = "productId can not be null")
    String productId;
    @NotNull(message = "consent can not be null")
    String consent;
    String nrega;
    String ckyc;
}
