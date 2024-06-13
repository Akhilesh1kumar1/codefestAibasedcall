package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GstSourceOutput implements Serializable {

    private String source;

    private String stateJurisdictionCode;

    private String status;

    private String taxpayerType;

    private String tradeName;

    private Object additionalPlaceOfBusinessFields;

    private String centreJurisdiction;

    private String centreJurisdictionCode;

    private String constitutionOfBusiness;

    private String dateOfCancellation;

    private String dateOfRegistration;

    private String gstin;

    private String gstinStatus;

    private String lastUpdatedDate;

    private String legalName;

    private List<String> natureOfBusinessActivity;

    private PrincipalPlaceOfBusinessFields principalPlaceOfBusinessFields;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PrincipalPlaceOfBusinessFields implements Serializable {

        private String natureOfPrincipalPlaceOfBusiness;

        private PrincipalPlaceOfBusinessAddress principalPlaceOfBusinessAddress;

    }


}
