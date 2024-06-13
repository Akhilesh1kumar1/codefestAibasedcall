package com.sr.capital.kyc.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sr.capital.helpers.enums.DocType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDocsDetailsRequest {

    @JsonProperty("sr_company_id")
    private String srCompanyId;

    @JsonProperty("kyc_status")
    private String kycStatus;

    @JsonProperty("doc_details")
    private List<DocDetails> docDetails;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DocDetails<T> {

        @JsonProperty("doc_type")
        private DocType docType;

        @JsonProperty("image_ids")
        private List<String> imageIds;

        @JsonProperty("details")
        private T details;
    }

}
