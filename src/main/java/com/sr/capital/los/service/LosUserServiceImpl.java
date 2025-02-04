package com.sr.capital.los.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.TenantDetails;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.los.LosStatusEntity;
import com.sr.capital.entity.mongo.los.LosUserEntity;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.service.IdService;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.KafkaEventTypes;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.los.dto.LosUserDTO;
import com.sr.capital.los.utill.ExternalCallStatusEnum;
import com.sr.capital.repository.mongo.los.LosStatusEntityRepository;
import com.sr.capital.repository.mongo.los.LosUserRepository;
import com.sr.capital.service.UserService;
import com.sr.capital.service.VerificationService;
import com.sr.capital.service.impl.VerificationUtilService;
import com.sr.capital.util.HashUtil;
import com.sr.capital.util.KafkaMessagePublisherUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.sr.capital.external.truthscreen.enums.TruthScreenDocType.PAN_COMPREHENSIVE;

@RequiredArgsConstructor
@Service
public class LosUserServiceImpl implements LosUserService{

    private final LosUserRepository losUserRepository;
    private final UserService userService;
    private final VerificationUtilService verificationUtilService;
    private final VerificationService verificationService;
    private final IdService idService;
    private final KafkaMessagePublisherUtil kafkaMessagePublisherUtil;
    private final AppProperties appProperties;
    private final LosStatusEntityRepository losStatusEntityRepository;

    private LosUserDTO convertToDTO(LosUserEntity user) {
        LosUserDTO dto =LosUserDTO.builder()
                .userId(user.getId())
                .mobile(user.getMobile())
                .email(user.getEmail())
                .typeOfEntity(user.getTypeOfEntity())
                .pan(user.getPan()).build();
        return dto;
    }

    private LosUserEntity convertToEntity(LosUserDTO dto) {
        return LosUserEntity.builder()
                .mobile(dto.getMobile())
                .email(dto.getEmail())
                .typeOfEntity(dto.getTypeOfEntity())
                .pan(dto.getPan()).build();
    }

    @Override
    public LosUserDTO getUserDetails(String token) {
        LosUserEntity losUserEntity = losUserRepository.findByUserId(String.valueOf(RequestData.getUserId()));
        if (losUserEntity != null) {
            return LosUserDTO.builder().userId(losUserEntity.getSrCompanyId())
                    .firstName(losUserEntity.getFirstName())
                    .lastName(losUserEntity.getLastName())
                    .email(losUserEntity.getEmail()).isMobileVerified(losUserEntity.getIsMobileVerified())
                    .mobile(losUserEntity.getMobile()).pan(losUserEntity.getPan())
                    .typeOfEntity(losUserEntity.getTypeOfEntity()).build();
        } else {
            InternalTokenUserDetailsResponse userDetailsUsingInternalToken = userService.getUserDetailsUsingInternalToken(token);
            if (userDetailsUsingInternalToken != null) {
                return LosUserDTO.builder().userId(userDetailsUsingInternalToken.getUserId()).email(userDetailsUsingInternalToken.getEmail())
                        .mobile(userDetailsUsingInternalToken.getMobile()).isMobileVerified(true)
                        .firstName(userDetailsUsingInternalToken.getFirstName())
                        .lastName(userDetailsUsingInternalToken.getLastName())
                        .build();
            }
        }
        return null;
    }

    @Override
    public Object verifyOtp(VerifyOtpRequest verifyOtpRequest) throws Exception {
        if (verificationService.verifyOtp(verifyOtpRequest)) {
            LosUserEntity losUserEntity = losUserRepository.findByUserId(String.valueOf(RequestData.getUserId()));
            if (losUserEntity != null) {
                losUserEntity.setIsMobileVerified(true);
                losUserRepository.save(losUserEntity);
            }
        }
        return null;
    }

    @Override
    public UUID generateOtp(String mobile) throws CustomException {
        saveDetailsIntoLosUserTable(mobile);

        TenantDetails tenantDetails =TenantDetails.builder().capitalUserId(RequestData.getUserId())
                .mobileNumber(mobile)
                .srUserId(RequestData.getUserId()).srCompanyId(RequestData.getTenantId()).build();


        VerificationOrchestratorRequest verificationOrchestratorRequest = VerificationOrchestratorRequest.builder()
                .rawRequest(
                        VerificationOrchestratorRequest.RawRequest.builder()
                                .callbackType(CallbackType.LOS_USER_VERIFICATION)
                                .verificationType(VerificationType.OTP)
                                .entityId(RequestData.getUserId())
                                .channel(CommunicationChannels.SMS_WHATSAPP)
                                .build()
                ).tenantDetails(tenantDetails)
                .build();

        verificationUtilService.createVerificationInstanceAndCommunicate(verificationOrchestratorRequest, RequestData.getTenantId());
        return verificationOrchestratorRequest.getVerificationEntity().getId();
    }

    private void saveDetailsIntoLosUserTable(String mobile) throws CustomException {
        LosUserEntity losUserEntity = losUserRepository.findByUserId(String.valueOf(RequestData.getUserId()));
        if (losUserEntity != null) {
            if (!mobile.equals(losUserEntity.getMobile())) {
                losUserEntity.setMobile(mobile);
                losUserRepository.save(losUserEntity);
            }
        } else {
            losUserEntity= LosUserEntity.builder()
                    .mobile(mobile)
                    .srCompanyId(RequestData.getTenantId())
                    .userId(String.valueOf(RequestData.getUserId()))
                    .build();
            losUserRepository.save(losUserEntity);
        }
    }

    @Override
    public IdSearchResponseDto<?> updateUser(LosUserDTO updatedLosUserDTO) throws Exception {
        LosUserEntity userEntity;
        LosUserEntity user = losUserRepository.findByUserId(String.valueOf(RequestData.getUserId()));
        if (user != null) {
            user.setEmail(updatedLosUserDTO.getEmail());
            user.setTypeOfEntity(updatedLosUserDTO.getTypeOfEntity());
            user.setPan(updatedLosUserDTO.getPan());
            user.setLoanAmount(updatedLosUserDTO.getLoanAmount());
            user.setFirstName(updatedLosUserDTO.getFirstName());
            user.setLastName(updatedLosUserDTO.getLastName());
            if (!user.getMobile().equals(updatedLosUserDTO.getMobile())) {
                user.setMobile(updatedLosUserDTO.getMobile());
            }
            userEntity = losUserRepository.save(user);
        } else {
            // if user try to go next without verifying mobile number
            LosUserEntity losUserEntity = LosUserEntity.builder()
                    .mobile(updatedLosUserDTO.getMobile())
                    .srCompanyId(RequestData.getTenantId())
                    .userId(String.valueOf(RequestData.getUserId()))
                    .email(updatedLosUserDTO.getEmail())
                    .pan(updatedLosUserDTO.getPan())
                    .firstName(updatedLosUserDTO.getFirstName())
                    .lastName(updatedLosUserDTO.getLastName())
                    .loanAmount(updatedLosUserDTO.getLoanAmount())
                    .typeOfEntity(updatedLosUserDTO.getTypeOfEntity())
                    .isMobileVerified(false)
                    .build();
            userEntity = losUserRepository.save(losUserEntity);
        }

        IdSearchResponseDto<?> idSearchResponseDto = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(), PAN_COMPREHENSIVE.getValue(), updatedLosUserDTO.getPan()));

        saveDetailsIntoEventStatusHandlerTable(userEntity.getUserId(), ExternalCallStatusEnum.PAN_COMPREHENSIVE_FETCHED.name());

        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName()
                ,kafkaMessagePublisherUtil.getKafkaMessage(userEntity.getUserId(),
                        KafkaEventTypes.LOS_EXTERNAL_CALL_EVENT.name(),null, RequestData.getCorrelationId(),null));


        return idSearchResponseDto;
    }

    private void saveDetailsIntoEventStatusHandlerTable(String userEntityId, String statusName) {
        LosStatusEntity losStatusEntity = losStatusEntityRepository.findByLosUserEntityId(userEntityId);

        if (losStatusEntity != null) {
            losStatusEntity.setStatus(statusName);
        } else {
            losStatusEntity = LosStatusEntity.builder()
                    .losUserEntityId(userEntityId)
                    .status(statusName)
                    .build();
        }
        losStatusEntityRepository.save(losStatusEntity);
    }
}
