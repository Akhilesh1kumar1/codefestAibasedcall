package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("lead")
public class Lead extends BaseDoc {

    @Indexed(unique = false,name = "srCompanyId")
    Long srCompanyId;

    BigDecimal amount;

    Integer duration;

    String status;
}
