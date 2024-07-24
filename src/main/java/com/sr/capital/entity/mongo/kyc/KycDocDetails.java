package com.sr.capital.entity.mongo.kyc;


import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.helpers.enums.DocStatus;
import com.sr.capital.helpers.enums.DocType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("kyc-doc-details")
/*@CompoundIndexes({
        @CompoundIndex(name = "srCompanyId_doc_type", def = "{'srCompanyId' : 1, 'docType': 1}")
})*/
public class KycDocDetails<T> extends BaseDoc {


    @Field("kyc_type")
    private KycType kycType;

   // @Indexed(unique = false,name = "srCompanyId")
    @Field("sr_company_id")
    private String srCompanyId;

    @Field("doc_type")
    private DocType docType;

    @Field("images")
    private List<String> images;

    @Builder.Default
    @Field("status")
    private DocStatus status = DocStatus.UNVERIFIED;

    @Field("details")
    private T details;

}
