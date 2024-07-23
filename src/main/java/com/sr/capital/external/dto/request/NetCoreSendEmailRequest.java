package com.sr.capital.external.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class NetCoreSendEmailRequest implements Serializable {

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("from")
    private AddressInfo fromAddressInfo;

    @JsonProperty("content")
    private List<ContentInfo> contentInfoList;

    @JsonProperty("personalizations")
    private List<Personalization> personalizations;

    @JsonProperty("attachments")
    private List<AttachmentInfo> attachments;
    @Data
    @Builder
    public static class AddressInfo {
        @JsonProperty("name")
        private String name;

        @JsonProperty("email")
        private String email;
    }

    @Data
    @Builder
    public static class ContentInfo {

        @Builder.Default
        @JsonProperty("type")
        private String type = "html";

        @JsonProperty("value")
        private String value;
    }

    @Data
    @Builder
    public static class AttachmentInfo {

        @JsonProperty("name")
        private String name ;

        @JsonProperty("content")
        private String content;
    }

    @Data
    @Builder
    public static class Personalization {
        @JsonProperty("attributes")
        private Map<String, String> attributes;

        @JsonProperty("to")
        private List<AddressInfo> toAddressInfo;

        @JsonProperty("attachments")
        List<AttachmentInfo> attachments;
    }
}
