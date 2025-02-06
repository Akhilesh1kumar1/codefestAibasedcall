package com.sr.capital.external.truthscreen.entity;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("gstin-details-history")
public class GSTinDetailsHistory extends BaseDoc {

    private String transId;
    @Field("sr_company_id")
    private String srCompanyId;
    @Field("gstin-details")
    private GSTinDetails gsTinDetails;

}
