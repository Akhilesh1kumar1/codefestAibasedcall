package com.sr.capital.entity.mongo.kyc;

import com.sr.capital.external.karza.request.GstConsolidationWebhookRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("gst-doc-details")
@Builder
@CompoundIndexes({
        @CompoundIndex(name = "srCompanyId_doc_type", def = "{'srCompanyId' : 1, 'gstInNumber': 1}")
})
public class GstCompleteDocDetails extends BaseDoc {

    Long srCompanyId;

   GstConsolidationWebhookRequest completeGstDetails;

    String gstInNumber;

}
