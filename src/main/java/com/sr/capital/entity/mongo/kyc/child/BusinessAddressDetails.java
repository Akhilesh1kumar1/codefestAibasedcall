package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessAddressDetails implements Serializable {

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

    String businessOwnerShipStatus;

    Boolean gstRegistered;

    Integer noOfDirector;

    List<BusinessPartnerInfo> businessPartnerInfo;
    @Data
    @Builder
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class BusinessPartnerInfo{

        String name;

        String panNumber;

        String gender;

        String dob;

        String mobileNumber;

        String address;

        String pincode;

        String businessPartnerHolding;

        String uniqueIdentifier;

        String city;

        String state;

    }


}
