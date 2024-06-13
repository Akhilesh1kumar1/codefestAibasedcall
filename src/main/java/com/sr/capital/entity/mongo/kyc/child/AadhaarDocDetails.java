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
public class AadhaarDocDetails implements Serializable {

    @Field("name_on_card")
    private String nameOnCard;

    @Field("fathers_name")
    private String fathersName;

    @Field("date_of_birth")
    private String dateOfBirth;

    @Field("year_of_birth")
    private String yearOfBirth;

    @Field("gender")
    private String gender;

    @Field("id_number")
    private String idNumber;

    @Field("house_number")
    private String houseNumber;

    @Field("street_address")
    private String streetAddress;

    @Field("address")
    private String address;

    @Field("district")
    private String district;

    @Field("state")
    private String state;

    @Field("pincode")
    private String pincode;

    @Field("is_scanned")
    private String isScanned;

}
