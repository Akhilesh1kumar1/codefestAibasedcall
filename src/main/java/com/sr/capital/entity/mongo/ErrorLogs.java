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

    String srCompanyId;

    String serviceName;

    Object requestBody;

    String requestParam;

    Object response;

    String statusCode;

    String groupName;

    String endPoint;

    String header;

    String errorMessage;
    Object error;
}
