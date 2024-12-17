package com.sr.capital.external.truthscreen.entity;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = false)
@Document("ts_trans_id_details")
@Builder
public class TsTransactionDocument {
    @Indexed(unique = true,name = "ts_trans_id")
    String ts_trans_id;
    String transId;
    String status;
    Map<String,Object> msg;
}
