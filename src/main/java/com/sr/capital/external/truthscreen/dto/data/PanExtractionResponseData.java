package com.sr.capital.external.truthscreen.dto.data;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data

public class PanExtractionResponseData {

    private String lastUpdate;
    private String name;
    private String nameOnTheCard;
    private String panHolderStatusType;
    private String docNumber;
    private String status;
    private String statusDescription;
    private int sourceId;
    //The data in this class should be same as add field for PAN
}
