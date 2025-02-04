package com.sr.capital.external.truthscreen.entity;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("ndd-details-history")
public class NddDetailsHistory extends BaseDoc {

    @Field("transId")
    private String transId;
    @Field("sr_company_id")
    private String srCompanyId;
    @Field("ndd-details-history")
    private NddDetails nddDetails;
}
