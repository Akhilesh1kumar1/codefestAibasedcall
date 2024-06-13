package com.sr.capital.entity.mongo.kyc;


import com.sr.capital.entity.mongo.kyc.child.ErrorDetails;
import com.sr.capital.helpers.enums.TaskType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("kyc-verified-details")
public class KycVerifiedDetails<T> extends BaseDoc {

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("task_type")
    private TaskType taskType;

    @Field("expires_at")
    private LocalDateTime expiresAt;

    @Field("details")
    private T details;

    @Field("error")
    private ErrorDetails error;
}
