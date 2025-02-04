package com.sr.capital.los.dto;

import com.sr.capital.los.utill.FormStatusEnum;
import lombok.Data;

@Data
public class FormProgressDTO {
    private String referenceId;
    private FormStatusEnum status;
}
