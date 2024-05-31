package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.CompanyType;
import com.sr.capital.helpers.enums.DocumentType;
import com.sr.capital.helpers.enums.ProofOfIdentity;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanyKycDetailsRequest extends BaseRequest{

    CompanyType companyType;

    Long adharId;

    Long panId;

    ProofOfIdentity proofOfIdentity;

    DocumentType documentType;

}
