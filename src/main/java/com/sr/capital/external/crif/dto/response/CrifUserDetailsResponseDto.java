package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class CrifUserDetailsResponseDto implements Serializable {
    private String userId;

    private String firstName;

    private String lastName;

    private String email;

    private String mobile;

    private String documentType;

    private String documentValue;

}
