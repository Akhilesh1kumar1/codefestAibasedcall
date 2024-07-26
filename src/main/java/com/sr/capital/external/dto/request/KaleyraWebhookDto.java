package com.sr.capital.external.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KaleyraWebhookDto {

    String from;

    String payload;

    String body;

    String replyTo;

    String mobile;

    String name;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Message {
        private List<ButtonMessage> message;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ButtonMessage {
        private Button button;
        private String caption;
        private String from;
        private String id;
        private Profile profile;
        private String type;
        private String wa_number;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Button {
        private String payload;
        private String text;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Profile {
        private String name;
    }

}
