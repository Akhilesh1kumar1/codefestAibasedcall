package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Document("feature_details")
@Builder
public class FeatureDetails extends BaseDoc {

    @Indexed(unique = false,name = "srCompanyId")
    Long srCompanyId;

    List<String> feature;
}
