package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class UtmDetailsDto {

    String utmSource;

    String utmMedium;

    String utmCampaign;

    String utmTerm;

    String utmContent;
}