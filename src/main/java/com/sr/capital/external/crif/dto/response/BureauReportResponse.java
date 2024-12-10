package com.sr.capital.external.crif.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class BureauReportResponse<T> implements Serializable {


    @Field("result")
    private T result;

    @Field("report_id")
    private String reportId;

    @Field("order_id")
    private String orderId;

    @Field("status_desc")
    private String statusDesc;

    @Field("status")
    private String status;
}