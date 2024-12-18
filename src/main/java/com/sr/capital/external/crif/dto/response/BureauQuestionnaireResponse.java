package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BureauQuestionnaireResponse implements Serializable {

    @JsonProperty("buttonbehaviour")
    private String buttonBehaviour;

    @JsonProperty("reportId")
    private String reportId;

    @JsonProperty("question")
    private String question;

    @JsonProperty("statusDesc")
    private String statusDesc;

    @JsonProperty("orderId")
    private String orderId;

    @JsonProperty("optionsList")
    private List<String> optionList;

    @JsonProperty("status")
    private String status;

    @JsonProperty("redirectURL")
    private String redirectURL;

    private String completedAt;

    private String createdAt;

    @Override
    public String toString() {
        return "BureauQuestionnaireResponse{" +
                "buttonBehaviour='" + buttonBehaviour + '\'' +
                ", reportId='" + reportId + '\'' +
                ", question='" + question + '\'' +
                ", orderId='" + orderId + '\'' +
                ", optionsList=" + optionList +
                ", status='" + status + '\'' +
                '}';
    }

}