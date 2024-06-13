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
public class PanGstCrossVerifiedDetails implements Serializable {

    @Field("status")
    private String status;

    @Field("gst_associated_with_pan")
    private boolean gstAssociatedWithPan;

    @Field("gst_details")
    List<GstDetails> gstDetails;

    @Data
    @Builder
    @JsonNaming(SnakeCaseStrategy.class)
    public static class GstDetails implements Serializable {

        @Field("gst_number")
        private String gstNumber;

        @Field("gstin_status")
        private String gstinStatus;

        @Field("state")
        private String state;
    }
}
