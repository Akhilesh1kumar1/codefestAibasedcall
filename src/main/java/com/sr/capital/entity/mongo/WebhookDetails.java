package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("loan_status_updates")
public class WebhookDetails extends BaseDoc {

    Long srCompanyId;

    UUID clientLoanId;

    Object loanWebhookData;

    String loanType;


}
