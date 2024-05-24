package com.sr.capital.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UpdateBaseCreditPartnerDto {

    @NotNull(message = "id can't be null")
    Long id;

    @NotNull(message = "creditPartnerName can't be null")
    String creditPartnerName;

    String description;

    String imageLink;

    @NotNull(message = "displayName can't be null")
    String displayName;

    Boolean enabled;
}
