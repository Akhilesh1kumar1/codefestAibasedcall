package com.sr.capital.entity.mongo;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("sales_data")
public class SalesData extends BaseDoc {

    @Field("month_cod_gmv")
    Long monthCodGmv;

    @Field("month_shipments")
    Long monthShipments;

    @Field("month_cod_shipments")
    Long monthCodShipments;

    @Field("month_remitted_value")
    Long monthRemittedValue;

    @Field("month_remit_awb_count")
    Long monthRemitAwbCount;

    @Field("month_gmv")
    Long shipmentGmv;
}
