package com.sr.capital.entity.mongo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.sr.capital.dto.response.UserProgressResponseDto;
import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.entity.mongo.kyc.child.Checkpoints;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document("loan_meta_data")
public class LoanMetaData extends BaseDoc {


    UUID loanId;

    String leadCode;

    String externalStatus1;

    String externalStatus2;

    String externalStatus3;

    String description;

    String externalApplicationStatus;

    List<Checkpoints> checkPoints;

}
