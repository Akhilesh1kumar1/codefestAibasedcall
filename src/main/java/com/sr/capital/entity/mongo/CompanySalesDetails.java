package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("company_sales_details")
public class CompanySalesDetails extends BaseDoc {

    Long srCompanyId;

    Map<String,SalesData> details;
}
