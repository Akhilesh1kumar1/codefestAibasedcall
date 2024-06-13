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
public class AadhaarVerifiedDetails implements Serializable {

    @Field("gender")
    private String gender;

    @Field("mobile_number")
    private String mobileNo;

    @Field("state")
    private String state;

    @Field("status")
    private String status;

}
