package com.sr.capital.entity.mongo.kyc.child;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GstDocDetails implements Serializable {

    @Field("legal_name")
    private String legalName;

    @Field("trade_name")
    private String tradeName;

    @Field("address")
    private String address;

    @Field("gstin")
    private String gstin;

    @Field("constitution_of_business")
    private String constitutionOfBusiness;

    @Field("type_of_registration")
    private String typeOfRegistration;

    @Field("pan_number")
    private String panNumber;

    @Field("date_of_liability")
    private String dateOfLiability;

    @Field("valid_up_to")
    private String validUpTo;

    @Field("is_provisional")
    private boolean isProvisional;

}
