package com.sr.capital.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CompanySalesDetails  {

    Long srCompanyId;

    Integer ageInSr;

    Integer platformAgeSignupInMonth;

    Integer platformAgeFtrInMonth;

    String orgKyc;

    Long monthCodGmv;

    Long monthShipments;

    Long monthCodShipments;

    Long monthRemittedValue;

    Long monthRemitAwbCount;

    Long shipmentGmv;

    Map<String,MonthlySalesDetails> detailsInfo;

}
