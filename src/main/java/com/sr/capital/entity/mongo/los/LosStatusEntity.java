package com.sr.capital.entity.mongo.los;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Document(collection = "users")
@Builder
@EqualsAndHashCode(callSuper = true)
@Getter
public class LosStatusEntity extends BaseDoc {

    @Field("los_user_id")
    private String losUserEntityId;
    @Field("status")
    private Set<String> status;

}
