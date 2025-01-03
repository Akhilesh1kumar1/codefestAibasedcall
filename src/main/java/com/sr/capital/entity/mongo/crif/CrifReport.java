package com.sr.capital.entity.mongo.crif;

import com.sr.capital.config.EncryptField;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.LinkedHashMap;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "crif_reports")
@Getter
public class CrifReport extends BaseDoc {

    @Field("result")
//    @EncryptField
    private Object result;

    @Field("report_id")
    private String reportId;

    @Field("mobile")
    @EncryptField
    private String mobile;

    @Field("order_id")
    private String orderId;

    @Field("status_desc")
    private String statusDesc;

    @Field("valid_till")
    private String validTill;

    @Field("status")
    private String status;

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("consent_id")
    private String consentId;
}