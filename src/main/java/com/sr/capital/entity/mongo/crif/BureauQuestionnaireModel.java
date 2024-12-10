package com.sr.capital.entity.mongo.crif;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("bureau-questionnaire-details")
public class BureauQuestionnaireModel<T> extends BaseDoc {

    @Field("redirect_url")
    private String redirectUrl;

    @Field("order_id")
    private String orderId;

    @Field("report_id")
    private String reportId;

    @Field("access_code")
    private String accessCode;

    @Field("status_desc")
    private String statusDesc;

    @Field("status")
    private String status;

    @Field("question")
    private String question;

    @Field("option_list")
    private String optionList;

    @Field("button_behavior")
    private String buttonBehavior;

    @Field("result")
    private T result;

    @Field("completed_at")
    private String completedAt;

}