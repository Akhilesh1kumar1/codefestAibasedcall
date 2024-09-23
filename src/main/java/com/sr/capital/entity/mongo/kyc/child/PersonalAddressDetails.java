package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.kyc.dto.request.PersonalAddressDetailsRequestDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PersonalAddressDetails implements Serializable {

    List<PersonalAddressDetails.Address> address;

    Map<String,String> metaData;

    KycType kycType;

    @Data
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Address {

        String addressType;

        String address;

        String city;

        String state;

        String pincode;

    }
}
