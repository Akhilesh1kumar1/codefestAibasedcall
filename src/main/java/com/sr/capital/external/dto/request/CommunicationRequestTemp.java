package com.sr.capital.external.dto.request;

import com.sr.capital.helpers.enums.CommunicationChannels;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldNameConstants;
import org.springframework.data.util.Pair;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@FieldNameConstants
public class CommunicationRequestTemp {

    private CommunicationChannels channel;

    private SmsCommunicationDTO smsCommunicationDto;

    private WhatsAppCommunicationDTO whatsAppCommunicationDto;

    private EmailCommunicationDTO emailCommunicationDto;

    private MetaData contentMetaData;

    private String templateName;

    @Data
    @Builder
    @FieldNameConstants
    public static class MetaData implements Serializable {

        private String loanId;

        private String capitalUrl;

        private String vendorName;

        private BigDecimal requestedLoanAmount;

        private Integer requestedLoanTenure;

        private BigDecimal approvedLoanAmount;

        private Date disbursmentDate;

        private String repaymentTerms;

        private String comments;

        private String invitationLink;
    }

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

        private String body;

        private List<String> params;
    }

    @Data
    @Builder
    @FieldNameConstants
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
