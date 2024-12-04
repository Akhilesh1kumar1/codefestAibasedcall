package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
//@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BureauInitiateResponse<T> implements Serializable {

    private String redirectUrl;

    private String orderId;

    private String reportId;

    private String accessCode;

    private String statusDesc;

    private String status;

    private T result;

    private String completedAt;

    private String createdAt;
}