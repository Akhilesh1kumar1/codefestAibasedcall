package com.sr.capital.external.flexi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PendingDocumentResponseDto {

    private Boolean success;
    private String message;
    private Data data;

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Data {
        private List<PendingItem> pendingList;

        @JsonProperty("co_applicants")
        private List<Object> coApplicants;

        private List<Object> guarantors;
        @JsonProperty("business_partners")
        private List<Object> businessPartners;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PendingItem {
        private String key;
        private String label;
        private List<RequiredItems> required;
        private List<OptionalItem> optional;

        @JsonProperty("optional_count")
        private Integer optionalCount;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OptionalItem {
        private String key;
        private String label;
        private Metadata metadata;

        @JsonProperty("document_guide")
        private Map<String, DocumentGuide> documentGuide;

    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Metadata {
        private String docLogo;
        private String remark;
        private boolean recommended;
    }
    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DocumentGuide {
        private String description;
    }

    @lombok.Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RequiredItems{
        private String key;
        private String label;
        private Metadata metadata;

        @JsonProperty("document_guide")
        private Map<String, DocumentGuide> documentGuide;
    }

}
