package com.sr.capital.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class PendingDocumentResponseDto {

    private List<PendingItem> pendingList;


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
        private Map<String,DocumentGuide> documentGuide;

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
