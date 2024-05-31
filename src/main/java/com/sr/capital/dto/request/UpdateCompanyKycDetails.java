package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.helpers.enums.CertificateOfIncorporationTypes;
import com.sr.capital.helpers.enums.ProofOfAddress;
import com.sr.capital.helpers.enums.ProofOfBusiness;
import com.sr.capital.helpers.enums.ProofOfCurrentAddress;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateCompanyKycDetails extends CompanyKycDetailsRequest{

    Long id;

    ProofOfAddress proofOfAddress;

    CertificateOfIncorporationTypes certificateOfIncorporationTypes;

    ProofOfBusiness proofOfBusiness;

    ProofOfCurrentAddress proofOfCurrentAddress;

}
