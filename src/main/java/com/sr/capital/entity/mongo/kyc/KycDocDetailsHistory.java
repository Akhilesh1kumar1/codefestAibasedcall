package com.sr.capital.entity.mongo.kyc;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@Document("kyc-doc-details-history")
public class KycDocDetailsHistory {

    @Id
    private String id;

    @Field("doc_details")
    private KycDocDetails<?> docDetails;
}
