package com.sr.capital.external.truthscreen.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("truthscreen-doc-details-history")
public class TruthScreenDocDetailsHistory {

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("doc_details")
    private TruthScreenDocDetails<?> docDetails;

}