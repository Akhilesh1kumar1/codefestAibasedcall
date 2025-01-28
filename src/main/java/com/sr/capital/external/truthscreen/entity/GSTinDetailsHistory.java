package com.sr.capital.external.truthscreen.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("gstin-details-history")
public class GSTinDetailsHistory {

    private String transId;

    @Field("gstin-details")
    private GSTinDetails gsTinDetails;

}
