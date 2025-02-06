package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class GstAnalyticsExtractionRequestData {

    private String transID;
    private int docType;
    private String docNumber;
    private String gstPortalUsername;
    private String fromDate;
    private String toDate;

}
