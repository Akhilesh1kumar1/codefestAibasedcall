package com.sr.capital.external.truthscreen.entity;


import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Document("pan_details")
@Builder
public class PanDetails {

    private String docNumber;
    private Map<String, Object> personalInfo;
    private String status;
    private String statusDescription;
    private String panHolderStatusType;


}
