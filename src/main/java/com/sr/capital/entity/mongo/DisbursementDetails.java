package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("disbursement_details")
public class DisbursementDetails extends BaseDoc {

    Long srCompanyId;

    UUID loanId;

   List<Object> disbursementDetails;
}
