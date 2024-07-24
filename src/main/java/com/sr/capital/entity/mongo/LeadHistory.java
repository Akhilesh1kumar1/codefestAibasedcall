package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.helpers.enums.LeadStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("lead_history")
public class LeadHistory extends BaseDoc {

    @Indexed(unique = false,name = "srCompanyId")
    Long srCompanyId;

    @Indexed(unique = false,name = "leadId")
    String leadId;

    BigDecimal amount;

    Integer duration;

    LeadStatus status;

    UUID loanApplicationId;

    String tier;

    String leadSource;

    String remarks;

    Long loanVendorPartnerId;

    String userName;
}
