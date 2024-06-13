package com.sr.capital.entity.mongo.kyc.child;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@JsonNaming(SnakeCaseStrategy.class)
public class PanVerifiedDetails implements Serializable {

    @Field("first_name")
    private String firstName;

    @Field("middle_name")
    private String middleName;

    @Field("last_name")
    private String lastName;

    @Field("name_on_card")
    private String nameOnCard;

    @Field("gender")
    private String gender;

    @Field("id_number")
    private String idNumber;

    @Field("source")
    private String source;

    @Field("aadhaar_seeding_status")
    private boolean aadhaarSeedingStatus;

    @Field("status")
    private String status;
}
