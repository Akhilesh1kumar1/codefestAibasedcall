package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.mongo.kyc.child.BusinessAddressDetails;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessDetailsRequestDto {


    String businessName;

    String businessPanNumber;

    String businessType;

    String industryType;

    String sectorType;

    String address1;

    String address2;

    String city;

    String state;

    String pincode;

    Map<String,Object> metaData;

    Boolean gstRegistered;

    String businessOwnerShipStatus;

    Integer noOfDirector;

    List<BusinessPartnerInfo> businessPartnerInfo;
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class BusinessPartnerInfo{

        String name;

        String panNumber;

        String gender;

        String dob;

        String mobileNumber;

        String address;

        String pincode;

        String businessPartnerHolding;



    }


}
