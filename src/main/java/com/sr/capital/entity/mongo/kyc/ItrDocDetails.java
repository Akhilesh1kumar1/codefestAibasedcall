package com.sr.capital.entity.mongo.kyc;

import com.sr.capital.kyc.external.response.extraction.ItrExtractionResponseData;
import com.sr.capital.kyc.external.response.extraction.data.ItrAdditionalResponseData;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("itr-doc-details")
@Builder

public class ItrDocDetails extends BaseDoc {

    @Indexed(unique = true)
    Long srCompanyId;

    ItrExtractionResponseData itrExtractionData;

    @Indexed(unique = true)
    String requestId;
}
