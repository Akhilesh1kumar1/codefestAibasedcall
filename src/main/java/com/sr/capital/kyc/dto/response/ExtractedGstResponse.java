package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.entity.mongo.kyc.child.GstDocDetails;
import com.sr.capital.kyc.external.constants.KarzaConstant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExtractedGstResponse {

    /*@JsonProperty("name")
    private String name;

    @JsonProperty("trade_name")
    private String tradeName;

    @JsonProperty("address")
    private String address;

    @JsonProperty("gstin")
    private String gstin;

    @JsonProperty("constitution_of_business")
    private String constitutionOfBusiness;

    @JsonProperty("type_of_registration")
    private String typeOfRegistration;

    @JsonProperty("pan_number")
    private String panNumber;

    @JsonProperty("date_of_liability")
    private String dateOfLiability;

    @JsonProperty("valid_up_to")
    private String validUpTo;

    @JsonProperty("is_provisional")
    private boolean isProvisional;*/
    String requestId;

    String gstin;

    String username;

    String refId;

    @Builder.Default
    String consent= KarzaConstant.DEFAULT_CONSENT;

    @Builder.Default
    boolean consolidate=false;

    @Builder.Default
    boolean extendedPeriod=false;


    List<GstUserDetails> gstUserDetails;


    @Builder
    @Data
    public static class GstUserDetails{

        String requestId;

        String gstin;

        String username;

        String refId;

        @Builder.Default
        String consent= KarzaConstant.DEFAULT_CONSENT;

        @Builder.Default
        boolean consolidate=false;

        @Builder.Default
        boolean extendedPeriod=false;
    }
}
