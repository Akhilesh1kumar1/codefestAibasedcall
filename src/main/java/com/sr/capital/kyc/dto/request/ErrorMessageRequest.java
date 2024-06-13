package com.sr.capital.kyc.dto.request;


import com.sr.capital.helpers.enums.TaskType;
import com.sr.capital.helpers.enums.VerificationType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorMessageRequest {

    @Builder.Default
    private VerificationType verificationType = VerificationType.DOC_VERIFICATION;

    private TaskType taskType;

    private String comments;
}
