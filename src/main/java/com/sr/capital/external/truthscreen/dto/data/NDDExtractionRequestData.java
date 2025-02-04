package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.external.truthscreen.enums.NddUserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NDDExtractionRequestData {

    @JsonProperty("transId")
    private int transId;
    @JsonProperty("source")
    private String source;
    @JsonProperty("docType")
    private int docType;
    @JsonProperty("name")
    private String name;
    @JsonProperty("address")
    private String address;
    @JsonProperty("fathername")
    private String fatherName;
    @JsonProperty("threshold")
    private String threshold;
    @JsonProperty("usertype")
    private String userType;
    @JsonProperty("all_database")
    private int allDatabase;
    @JsonProperty("database_list")
    private String databaseList;

}
