package com.sr.capital.service.impl;


import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.entity.primary.VerificationEntity;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.service.entityimpl.VerificationEntityServiceImpl;
import com.sr.capital.util.HashUtil;
import com.sr.capital.util.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VerificationUtilService {

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private VerificationEntityServiceImpl verificationManager;

    @Autowired
    private CommunicationService communicationService;

    public void createVerificationInstanceAndCommunicate(VerificationOrchestratorRequest verificationOrchestratorRequest) throws CustomException {

        VerificationOrchestratorRequest.RawRequest rawRequest = verificationOrchestratorRequest.getRawRequest();
        VerificationEntity verificationEntity = VerificationEntity.builder()
                .type(rawRequest.getVerificationType())
                .srCompanyId(String.valueOf(RequestData.getTenantId())).userId(verificationOrchestratorRequest.getTenantDetails().getCapitalUserId())
                .callback(rawRequest.getCallbackType())
                .channel(rawRequest.getChannel())
                .build();
        if(VerificationType.LINK.equals(rawRequest.getVerificationType())){
            try {
                verificationEntity.setData(HashUtil.generateEmailVerificationData());
                verificationEntity.setExpiresAt(LocalDateTime.now().plusHours(24));
            } catch (Exception e) {
                throw new CustomException("Hash Exception");
            }
        } else {
            verificationEntity.setData(OtpGenerator.generateOTP());
            if(!"prod".equals(appProperties.getActiveProfile())) {
                verificationEntity.setData("123456");
            }
        }
        verificationEntity.setIsEnabled(true);
        verificationManager.saveVerificationEntity(verificationEntity);
        verificationOrchestratorRequest.setVerificationEntity(verificationEntity);

        if (verificationOrchestratorRequest.getShouldCommunicate()) {
            String mobile = getMobileNo(verificationOrchestratorRequest);
            switch (rawRequest.getChannel()) {
                case EMAIL -> {
                    String verificationLink = "test" + "/" + verificationEntity.getId() + "/" + verificationEntity.getData();
                    String email = verificationOrchestratorRequest.getTenantDetails().getEmailId();
                    String name = verificationOrchestratorRequest.getTenantDetails().getName();
                    communicationService.sendEmail(communicationService.getCommunicationRequestForEmailVerification(email, name, verificationLink));
                } case SMS -> {
                    if(StringUtils.hasText(mobile)) {
                        communicationService.sendOtpForVerification(communicationService.getCommunicationRequestForOtpVerificationViaSms(mobile, verificationEntity.getData()));
                    }
                } case WHATSAPP -> {
                    if(StringUtils.hasText(mobile)) {
                        communicationService.sendOtpForVerification(communicationService.getCommunicationRequestForOtpVerificationViaWhatsApp(mobile, List.of(verificationEntity.getData())));
                    }
                } case SMS_WHATSAPP -> {
                    if(StringUtils.hasText(mobile)) {
                        communicationService.sendOtpForVerification(communicationService.getCommunicationRequestForOtpVerificationViaSmsAndWhatsApp(mobile, verificationEntity.getData(), List.of(verificationEntity.getData())));
                    }
                }
            }
        }
    }


    private String getMobileNo(VerificationOrchestratorRequest orchestratorRequest) {
       return orchestratorRequest.getTenantDetails().getMobileNumber();
    }
}
