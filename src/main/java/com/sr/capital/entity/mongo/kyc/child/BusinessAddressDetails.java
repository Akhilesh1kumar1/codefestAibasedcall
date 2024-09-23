package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BusinessAddressDetails implements Serializable {

    String businessName;

    String businessPanNumber;

    String businessType;

    String industryType;

    String sectorType;

    String address;

    String city;

    String state;

    String pincode;

    Map<String,Object> metaData;
}
