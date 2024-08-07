package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.offer.calculator.helpers.ParameterName;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("underwriting_config_history")
public class UnderWritingConfigHistory extends BaseDoc {

    private String groupName;

    @Enumerated(EnumType.STRING)
    private ParameterName name;

    private double value;

    private double score;

    private String condition;

    private Double weightage;
}
