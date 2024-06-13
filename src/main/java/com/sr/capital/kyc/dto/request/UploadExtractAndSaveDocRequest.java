package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sr.capital.entity.mongo.kyc.KycDocDetails;
import com.sr.capital.external.shiprocket.enums.KycType;
import com.sr.capital.helpers.enums.DocActionType;
import com.sr.capital.helpers.enums.DocType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Data
public class UploadExtractAndSaveDocRequest<T> {

    @NotBlank(message = "srCompanyId is required!")
    @JsonProperty("sr_company_id")
    private String srCompanyId;

    @NotNull(message = "Doc Type is required!")
    @JsonProperty("doc_type")
    private DocType docType;

    @NotEmpty(message = "Actions are required!")
    @JsonProperty("actions")
    private List<DocActionType> actions;

    private Map<String, String> docDetails;

    private KycType kycType;
}
