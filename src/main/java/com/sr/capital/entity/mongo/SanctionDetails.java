package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("sanction_details")
public class SanctionDetails extends BaseDoc {

    Long srCompanyId;

    UUID loanId;

    String vendorSanctionCode;

    Object sanctionDetails;
}
