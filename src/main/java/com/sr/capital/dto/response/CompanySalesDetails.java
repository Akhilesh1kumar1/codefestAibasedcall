package com.sr.capital.dto.response;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
public class CompanySalesDetails  {

    Long srCompanyId;

    Long ageInSr;

    Long platformAgeSignupInMonth;

    Long platformAgeFtrInMonth;

    String orgKyc;

    Long monthCodGmv;

    Long monthShipments;

    Long monthCodShipments;

    Long monthRemittedValue;

    Long monthRemitAwbCount;

    Long shipmentGmv;
}
