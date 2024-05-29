package com.sr.capital.external.dto.request;

import com.sr.capital.helpers.enums.CommunicationChannels;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class CommunicationRequestTemp {

    private CommunicationChannels channel;

    private SmsCommunicationDTO smsCommunicationDto;

    private WhatsAppCommunicationDTO whatsAppCommunicationDto;

    private EmailCommunicationDTO emailCommunicationDto;

    @Data
    @Builder
    public static class SmsCommunicationDTO implements Serializable {

        private String recipientNo;

        private String template;

        private String body;
    }

    @Data
    @Builder
    public static class WhatsAppCommunicationDTO implements Serializable {

        private String recipientNo;

        private String template;

        private List<String> params;
    }

    @Data
    @Builder
    public static class EmailCommunicationDTO implements Serializable {

        private String recipientEmail;

        private String recipientName;

        private List<Pair<String, String>> ccRecipientEmailNamePairList;

        private List<Pair<String, String>> bccRecipientEmailNamePairList;

        private String subject;

        private String body;

        private Map<String, String> params;
    }

}
