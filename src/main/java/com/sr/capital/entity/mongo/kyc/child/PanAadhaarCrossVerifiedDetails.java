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
public class PanAadhaarCrossVerifiedDetails implements Serializable {

    @Field("is_linked")
    private Boolean isLinked;

    @Field("message")
    private String message;

    @Field("status")
    private String status;
}
