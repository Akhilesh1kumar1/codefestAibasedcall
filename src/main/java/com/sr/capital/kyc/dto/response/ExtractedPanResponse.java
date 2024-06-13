package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExtractedPanResponse {

    @JsonProperty("name")
    private String name;

    @JsonProperty("fathers_name")
    private String fathersName;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("age")
    private String age;

    @JsonProperty("minor")
    private boolean minor;

    @JsonProperty("id_number")
    private String idNumber;

    @JsonProperty("pan_type")
    private String panType;

    @JsonProperty("date_of_issue")
    private String dateOfIssue;
}
