package com.sr.capital.entity.mongo.kyc;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(callSuper = true)
@Document("itr-doc-history")
@Builder

public class ItrDocDetailsHistory extends BaseDoc {

    @Indexed(unique = false)
    Long srCompanyId;

    ItrDocDetails itrDocDetails;
}
