package com.sr.capital.dto.request;


import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.VerificationType;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class VerificationOrchestratorRequest {
    @Builder.Default
    private Boolean shouldCommunicate = Boolean.TRUE;
    private VerifyOtpRequest verifyOtpRequest;
    private VerificationEntity verificationEntity;
    private RawRequest rawRequest;
    private TenantDetails tenantDetails;

    @Data
    @Builder
    public static class  RawRequest {
        private VerificationType verificationType;
        private CallbackType callbackType;
        private Long entityId;
        @Builder.Default
        private CommunicationChannels channel = CommunicationChannels.SMS_WHATSAPP;
    }
}
