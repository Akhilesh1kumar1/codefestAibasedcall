package com.sr.capital.entity.mongo.crif;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@Document("bureau-init-details")
public class BureauInitiateModel extends BaseDoc {

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

    @Field("question")
    private String question;

    @Field("user_answer")
    private String userAnswer;

    @Field("option_list")
    private String optionList;

    @Field("button_behavior")
    private String buttonBehavior;

    @Field("completed_at")
    private String completedAt;
}