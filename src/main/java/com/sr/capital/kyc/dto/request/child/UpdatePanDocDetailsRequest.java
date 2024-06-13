package com.sr.capital.kyc.dto.request.child;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UpdatePanDocDetailsRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("date_of_birth")
    private String dateOfBirth;

    @JsonProperty("id_number")
    private String idNumber;
}
