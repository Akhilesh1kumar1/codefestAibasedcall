package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDirector {

    @JsonProperty("has_director_check")
    private Boolean hasDirectorCheck;

    @JsonProperty("has_past_director")
    private Boolean hasPastDirector;

    @JsonProperty("has_current_director")
    private Boolean hasCurrentDirector;

    @JsonProperty("name")
    private String name;
}
