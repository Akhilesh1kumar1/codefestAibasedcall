package com.sr.capital.entity.mongo.crif;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("bureau-init-details")
public class BureauInitiateModel<T> extends BaseDoc {

    @Field("redirect_url")
    private String redirectUrl;

    @Field("report_id")
    private String reportId;

    @Field("order_id")
    private String orderId;

    @Field("status_code")
    private String statusCode;

    @Field("status_desc")
    private String statusDesc;

    @Field("access_code")
    private String accessCode;

    @Field("details")
    private T details;

}