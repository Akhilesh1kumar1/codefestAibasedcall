package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BureauInitiateResponse<T> implements Serializable {

    private String redirectURL;

    private String orderId;

    private String reportId;

    private String accessCode;

    private String statusDesc;

    private String status;

    private String userAnswer;

    private T result;

    private T data;

    private String completedAt;

    private String createdAt;

    @Override
    public String toString() {
        return "BureauInitiateResponse{" +
                "redirectUrl='" + redirectURL + '\'' +
                ", orderId='" + orderId + '\'' +
                ", reportId='" + reportId + '\'' +
                ", accessCode='" + accessCode + '\'' +
                ", statusDesc='" + statusDesc + '\'' +
                ", status='" + status + '\'' +
                ", result=" + result +
                ", data=" + data +
                ", completedAt='" + completedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

}