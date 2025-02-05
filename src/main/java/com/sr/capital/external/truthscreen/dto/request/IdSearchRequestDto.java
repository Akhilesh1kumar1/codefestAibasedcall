package com.sr.capital.external.truthscreen.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@AllArgsConstructor
public class IdSearchRequestDto {

    @JsonProperty("trans_id")
    String transId;

    @JsonProperty("doc_type")
    int docType;

    @JsonProperty("doc_number")
    String docNumber;

    @JsonProperty("gst_portal_username")
    String gstPortalUsername;

    @JsonProperty("from_date")
    String fromDate;

    @JsonProperty("to_data")
    String toDate;


}
