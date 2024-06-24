package com.sr.capital.kyc.external.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KarzaBaseResponse<T> implements Serializable {

    private String action;

    private String status;

    private String type;

    private String taskId;

    private String groupId;

    private String requestId;

    private String completedAt;

    private String createdAt;

    private T result;

    private String error;

    private String message;

}
