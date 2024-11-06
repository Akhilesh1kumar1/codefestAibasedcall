package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("error_logs")
public class ErrorLogs extends BaseDoc {

    Long srCompanyId;

    Long vendorId;

    Object request;

    Object response;

    String statusCode;

    String groupName;
}
