package com.sr.capital.validation;


import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.ResendOtpRequest;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.exception.custom.InvalidCommunicationChannelException;
import com.sr.capital.exception.verification.InvalidVerificationTokenException;
import com.sr.capital.exception.verification.OtpRequestLimitException;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.service.RequestValidator;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.service.impl.CryptoService;
import com.sr.capital.util.CoreUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Component
public class ResendOtpRequestValidator implements RequestValidator {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private CryptoService cryptoService;

    @Override
    @SuppressWarnings("unchecked")
    public <T, U> T validateRequest(U request) throws Exception {
        ResendOtpRequest otpRequest = (ResendOtpRequest) request;

        if(Boolean.TRUE.equals(appProperties.getRsaEncryptionEnabled())) {
            otpRequest.setMobile(cryptoService.decryptDataUsingRsaPrivateKey(otpRequest.getMobile()));
        }

        VerificationEntity verificationEntity = verificationManager.findByVerificationId(otpRequest.getVerificationToken());
        if(ObjectUtils.isEmpty(verificationEntity) || Boolean.FALSE.equals(verificationEntity.getIsEnabled())
                || !VerificationType.OTP.equals(verificationEntity.getType())) {
            throw new InvalidVerificationTokenException();
        }
        if(verificationEntity.getRequestCounter() >= appProperties.getOtpMaxRequestCount()){
            throw new OtpRequestLimitException();
        }
        CommunicationChannels channel = otpRequest.getChannel() != null ? otpRequest.getChannel() : verificationEntity.getChannel();
        if(verificationEntity.getCallback().equals(CallbackType.USER_SIGN_UP)) {
            if(!channel.equals(CommunicationChannels.SMS)) {
                throw new InvalidCommunicationChannelException();
            }
            if(StringUtils.hasText(otpRequest.getMobile())) {
                CoreUtil.validateMobile(otpRequest.getMobile());
            }
        }

        verificationEntity.setChannel(channel);
        return (T) verificationEntity;
    }
}
