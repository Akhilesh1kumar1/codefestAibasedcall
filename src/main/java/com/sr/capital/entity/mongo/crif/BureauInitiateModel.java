package com.sr.capital.entity.mongo.crif;

import com.sr.capital.config.EncryptField;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document("bureau_init_details")
public class BureauInitiateModel extends BaseDoc {

    @Field("redirect_url")
    private String redirectUrl;

    @Field("report_id")
    private String reportId;

    @Field("mobile")
    @EncryptField
    private String mobile;

    @Field("order_id")
    private String orderId;

    @Field("status_code")
    private String statusCode;

    @Field("status_desc")
    private String statusDesc;

    @Field("sr_company_id")
    private String srCompanyId;

    @Field("request_payload")
    private String requestPayload;

    @Field("request_header")
    private String requestHeader;

    @Field("init_response")
    private Object initResponse;

    @Field("questionnaire_response")
    private Object questionnaireResponse;

    @Field("init_status")
    private String initStatus;

    @Field("questionnaire_status")
    private String questionnaireStatus;

    @Field("last_question")
    private String lastQuestion;

    @Field("user_answer")
    private String userAnswer;

    @Field("question_option_list")
    private List<Map<String, List<String>>> optionList;

    @Field("button_behavior")
    private String buttonBehavior;

    @Field("completed_at")
    private String completedAt;
}