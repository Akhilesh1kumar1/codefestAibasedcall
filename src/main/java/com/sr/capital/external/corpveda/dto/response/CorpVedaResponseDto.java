package com.sr.capital.external.corpveda.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Data
@SuperBuilder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
@NoArgsConstructor
public class CorpVedaResponseDto {

    private String srCompanyId;
    private String cin;
    private String companyPan;
    private String registrationNumber;
    private String companyName;
    private String telephone;
    private String email;
    private String authorizedCapital;
    private String initialStatus;
}
