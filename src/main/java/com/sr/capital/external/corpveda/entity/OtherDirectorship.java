package com.sr.capital.external.corpveda.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class OtherDirectorship {

    @JsonProperty("name")
    private String name;

    @JsonProperty("din")
    private String din;

    @JsonProperty("present_directorships")
    private List<PresentDirectorship> presentDirectorships;

    @JsonProperty("past_directorships")
    private List<PastDirectorship> pastDirectorships;
}
