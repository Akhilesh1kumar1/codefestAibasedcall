package com.sr.capital.external.truthscreen.entity;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.enums.TruthScreenState;
import com.sr.capital.external.truthscreen.enums.TruthScreenStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("truthscreen-doc-details")
public class TruthScreenDocDetails<T> extends BaseDoc {

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("trans_id")
    private String transId;

    @Field("final_ts_trans_id")
    private String finalTsTransId;

    @Field("truth_doc_type")
    private TruthScreenDocType truthScreenDocType;

    @Field("details")
    private T details;

    @Field("initial_status")
    TruthScreenStatus initialStatus;

    @Field("state")
    TruthScreenState state;


}
