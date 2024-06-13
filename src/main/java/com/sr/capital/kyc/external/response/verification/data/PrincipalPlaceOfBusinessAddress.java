package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PrincipalPlaceOfBusinessAddress implements Serializable {

    private String buildingName;

    private String city;

    private String doorNumber;

    private String dst;

    private String floorNumber;

    private String latitude;

    private String location;

    private String longitude;

    private String pincode;

    private String stateName;

    private String street;

}
