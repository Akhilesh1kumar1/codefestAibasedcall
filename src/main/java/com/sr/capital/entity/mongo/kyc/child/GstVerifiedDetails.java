package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class GstVerifiedDetails implements Serializable {

    @Field("legal_name")
    private String legalName;

    @Field("trade_name")
    private String tradeName;

    @Field("gstin")
    private String gstin;

    @Field("gstin_status")
    private String gstinStatus;

    @Field("tax_payer_type")
    private String taxpayerType;

    @Field("constitution_of_business")
    private String constitutionOfBusiness;

    @Field("state_jurisdiction_code")
    private String stateJurisdictionCode;

    @Field("centre_jurisdiction")
    private String centreJurisdiction;

    @Field("centre_jurisdiction_code")
    private String centreJurisdictionCode;

    @Field("date_of_registration")
    private String dateOfRegistration;

    @Field("last_updated_date")
    private String lastUpdatedDate;

    @Field("date_of_cancellation")
    private String dateOfCancellation;

    @Field("source")
    private String source;

    @Field("status")
    private String status;

    @Field("nature_of_business_activity")
    private List<String> natureOfBusinessActivity;

    @Field("additional_place_of_business_fields")
    private Object additionalPlaceOfBusinessFields;

    @Field("nature_of_principal_place_of_business")
    private String natureOfPrincipalPlaceOfBusiness;

    @Field("door_number")
    private String doorNumber;

    @Field("building_name")
    private String buildingName;

    @Field("floor_number")
    private String floorNumber;

    @Field("street")
    private String street;

    @Field("city")
    private String city;

    @Field("state_name")
    private String stateName;

    @Field("pincode")
    private String pincode;

    @Field("dst")
    private String dst;

    @Field("latitude")
    private String latitude;

    @Field("longitude")
    private String longitude;

    @Field("location")
    private String location;

}
