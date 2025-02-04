package com.sr.capital.los.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class LosUserDTO {
    private String userId;
    private String srCompanyId;
    private String mobile;
    private String email;
    private String typeOfEntity;
    private String pan;
    private BigDecimal loanAmount;
    private Boolean isMobileVerified;
}
