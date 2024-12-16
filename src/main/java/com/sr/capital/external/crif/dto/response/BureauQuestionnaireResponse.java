package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BureauQuestionnaireResponse implements Serializable {

    @JsonProperty("redirectURL")
    private String redirectURL;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("reportId")
    private String reportId;

    private String accessCode;

    @JsonProperty("statusDesc")
    private String statusDesc;

    private String status;

    private String question;

    @JsonProperty("optionsList")
    private String optionList;

    @JsonProperty("buttonbehaviour")
    private String buttonBehaviour;

    @Override
    public String toString() {
        return "BureauQuestionnaireResponse{" +
                "redirectUrl='" + redirectURL + '\'' +
                ", orderId='" + orderId + '\'' +
                ", reportId='" + reportId + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", status='" + status + '\'' +
                ", question='" + question + '\'' +
                ", optionList='" + optionList + '\'' +
                ", buttonBehavior='" + buttonBehaviour + '\'' +
                ", completedAt='" + completedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    private String completedAt;

    private String createdAt;
}