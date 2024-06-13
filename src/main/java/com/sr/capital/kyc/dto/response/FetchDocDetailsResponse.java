package com.sr.capital.kyc.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.sr.capital.helpers.enums.DocStatus;
import com.sr.capital.helpers.enums.DocType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FetchDocDetailsResponse<T> {

    @JsonProperty("sr_company_id")
    private String srCompanyId;

    @JsonProperty("doc_type")
    private DocType docType;

    @JsonProperty("images")
    private List<String> images;

    @JsonProperty("image_ids")
    private List<String> imageIds;

    @JsonProperty("status")
    private DocStatus status;

    @JsonProperty("details")
    private T details;

    @JsonProperty("last_modified_at")
    private String lastModifiedAt;

}
