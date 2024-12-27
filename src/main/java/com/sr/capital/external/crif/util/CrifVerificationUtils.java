package com.sr.capital.external.crif.util;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.TenantDetails;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.service.VerificationService;
import com.sr.capital.service.impl.VerificationUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrifVerificationUtils {

    private final VerificationService verificationService;

    private final AES256 aes256;

    private final VerificationUtilService verificationUtilService;




    public VerificationOrchestratorRequest sendOtp(CrifUserModel userDetails) throws CustomException {
        TenantDetails tenantDetails =TenantDetails.builder().capitalUserId(RequestData.getUserId())
                .mobileNumber(userDetails.getMobile())
                .srUserId(RequestData.getUserId()).emailId(userDetails.getEmail()).build();

        VerificationOrchestratorRequest verificationOrchestratorRequest = VerificationOrchestratorRequest.builder()
                .rawRequest(
                        VerificationOrchestratorRequest.RawRequest.builder()
                                .callbackType(CallbackType.CRIF_VERIFICATION)
                                .verificationType(VerificationType.OTP)
                                .entityId(RequestData.getUserId())
                                .channel(CommunicationChannels.SMS_WHATSAPP)
                                .build()
                ).tenantDetails(tenantDetails)
                .build();

        verificationUtilService.createVerificationInstanceAndCommunicate(verificationOrchestratorRequest);
        return verificationOrchestratorRequest;
    }

    public Boolean verifyOtp(VerifyOtpRequest crifGenerateOtpRequestModel) {
        try {
            return verificationService.verifyOtp(crifGenerateOtpRequestModel);
        } catch (Exception e) {
            throw new RuntimeException("Error while verifying otp " + e.getMessage());
        }
    }
}
