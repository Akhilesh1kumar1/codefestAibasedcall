package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

@Data
//@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthlySalesDetails {

    Long monthCodGmv;

    Long monthShipments;

    Long monthCodShipments;

    Long monthRemittedValue;

    Long monthRemitAwbCount;

    Long shipmentGmv;
}
