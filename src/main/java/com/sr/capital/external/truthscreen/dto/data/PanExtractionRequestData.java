package com.sr.capital.external.truthscreen.dto.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PanExtractionRequestData
{

        private String transId;
        private int docType;
        private String docNumber;

        //The data in this class should be same as add field for PAN

}
