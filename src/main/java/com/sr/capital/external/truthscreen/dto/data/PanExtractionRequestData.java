package com.sr.capital.external.truthscreen.dto.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PanExtractionRequestData
{

        private String transID;
        private int docType;
        private String docNumber;

        //The data in this class should be same as add field for PAN

}
