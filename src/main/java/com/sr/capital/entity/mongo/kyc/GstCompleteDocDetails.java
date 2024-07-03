package com.sr.capital.entity.mongo.kyc;

import com.sr.capital.external.karza.request.GstConsolidationWebhookRequest;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("gst-doc-details")
@Builder
public class GstCompleteDocDetails extends BaseDoc {

    Long srCompanyId;

   GstConsolidationWebhookRequest completeGstDetails;

    String gstInNumber;

}
