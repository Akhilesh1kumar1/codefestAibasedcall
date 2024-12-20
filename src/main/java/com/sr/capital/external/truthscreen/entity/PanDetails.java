package com.sr.capital.external.truthscreen.entity;


import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PanDetails {

    private String lastUpdate;
    private String name;
    private String nameOnTheCard;
    private String panHolderStatusType;
    private String docNumber;
    private String status;
    private String statusDescription;
    private int sourceId;


}
