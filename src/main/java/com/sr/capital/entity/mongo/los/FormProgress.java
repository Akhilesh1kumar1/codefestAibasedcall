package com.sr.capital.entity.mongo.los;

import com.sr.capital.entity.mongo.kyc.BaseDoc;
import com.sr.capital.los.utill.FormStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "form_progress")
@AllArgsConstructor
@Builder
public class FormProgress extends BaseDoc {

    private String referenceId;
    private FormStatusEnum status;
}

