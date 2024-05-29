package com.sr.capital.external.dto.request;

import com.sr.capital.helpers.enums.CommunicationChannels;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CommunicationRequest {

    String method;

    String from;

    String to;

    String message;

    String templateId;

    String channelName;

    CommunicationChannels channel;

    String requestId;

    Map<String,String> metaData;
}
