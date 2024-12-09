package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("feature_details")
public class FeatureDetails extends BaseDoc {

    @Indexed(unique = false,name = "srCompanyId")
    String srCompanyId;

    List<String> feature;
}
