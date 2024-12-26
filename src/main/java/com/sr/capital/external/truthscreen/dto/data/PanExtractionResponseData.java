package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanExtractionResponseData {

    @JsonProperty("LastUpdate")
    private String lastUpdate;
    @JsonProperty("Name")
    private String name;
    @JsonProperty("NameOnTheCard")
    private String nameOnTheCard;
    @JsonProperty("PanHolderStatusType")
    private String panHolderStatusType;
    @JsonProperty("PanNumber")
    private String panNumber;
    @JsonProperty("STATUS")
    private String status;
    @JsonProperty("StatusDescription")
    private String statusDescription;
    @JsonProperty("source_id")
    private Integer sourceId;
    //The data in this class should be same as add field for PAN
}
