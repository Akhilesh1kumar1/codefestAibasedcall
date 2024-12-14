package com.sr.capital.entity.mongo.crif;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "b2c_reports")
@Getter
public class CrifReport extends BaseDoc {

    @Field("result")
    private Object result;

    @Field("report_id")
    private String reportId;

    @Field("mobile")
    private String mobile;

    @Field("order_id")
    private String orderId;

    @Field("status_desc")
    private String statusDesc;

    @Field("valid_till")
    private String validTill;

    @Field("status")
    private String status;


}