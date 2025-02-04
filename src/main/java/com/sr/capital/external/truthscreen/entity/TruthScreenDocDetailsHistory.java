package com.sr.capital.external.truthscreen.entity;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("truthscreen-doc-details-history")
public class TruthScreenDocDetailsHistory extends BaseDoc {

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("doc_details")
    private TruthScreenDocDetails<?> docDetails;

}