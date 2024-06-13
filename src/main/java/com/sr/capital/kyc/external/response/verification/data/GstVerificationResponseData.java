package com.sr.capital.kyc.external.response.verification.data;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GstVerificationResponseData implements Serializable {

    private GstSourceOutput sourceOutput;

}
