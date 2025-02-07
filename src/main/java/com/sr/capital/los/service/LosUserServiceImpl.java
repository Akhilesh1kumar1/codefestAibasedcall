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
import com.sr.capital.external.truthscreen.entity.Address;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.service.IdService;
import com.sr.capital.helpers.enums.CallbackType;
import com.sr.capital.helpers.enums.CommunicationChannels;
import com.sr.capital.helpers.enums.KafkaEventTypes;
import com.sr.capital.helpers.enums.VerificationType;
import com.sr.capital.kyc.manager.KycDocDetailsManager;
import com.sr.capital.los.dto.LosUserDTO;
import com.sr.capital.los.dto.ScreenNameDTO;
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

import java.util.Set;
import java.util.UUID;

import static com.sr.capital.entity.mongo.los.LosUserEntity.REFERENCE_ID_SEQUENCE;
import static com.sr.capital.external.truthscreen.enums.TruthScreenDocType.*;
import static com.sr.capital.los.utill.ExternalCallStatusEnum.*;

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
    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;
    private final SequenceGeneratorService sequenceGeneratorService;
    private final KycDocDetailsManager kycDocDetailsManager;

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
        LosUserEntity losUserEntity = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
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
            LosUserEntity losUserEntity = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
            if (losUserEntity != null) {
                losUserEntity.setIsMobileVerified(true);
                losUserRepository.save(losUserEntity);
            }
            return true;
        }
        return false;
    }

    @Override
    public ScreenNameDTO getScreenData(String token) throws Exception {
        LosUserEntity user = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
        if (user != null) {
            LosStatusEntity losStatusEntity = losStatusEntityRepository.findByLosUserEntityId(user.getId());
            if (losStatusEntity != null) {
                ScreenNameDTO screenStatus = getScreenStatus(losStatusEntity, user.getScreenName());
                if (screenStatus != null) return screenStatus;
            }
        }

        LosUserDTO userDetails = getUserDetails(token);
        return convertIntoScreenNameDto(BASIC_DETAILS, userDetails);
    }


    public static ScreenNameDTO convertIntoScreenNameDto(ExternalCallStatusEnum screenName, Object data) {
        ScreenNameDTO dto = ScreenNameDTO.builder().build();
        dto.setScreenName(screenName.name());

        switch (screenName) {
            case BASIC_DETAILS:
                dto.setBasicDetails(data);
                break;

            case PAN_COMPREHENSIVE_FETCHED:
                dto.setPersonalDetails(data);
                break;

            case PAN_TO_GST_FETCHED:
                dto.setBusinessDetails(data);
                break;

            case GST_ANALYTICS_DONE:
                dto.setDocumentUpload(data);
                break;

            case CORPVEDA_DETAILS_FETCHED:
                dto.setOfferGeneration(data);
                break;

//            case OFFER_ACCEPTANCE:
//                dto.setOfferAcceptance(data);
//                break;

            default:
                throw new IllegalArgumentException("Unsupported screen name: " + screenName);
        }
        return dto;
    }

    public ScreenNameDTO getScreenStatus(LosStatusEntity losStatusEntity, String screenName) throws Exception {

        if (losStatusEntity != null) {
            Set<String> formStatuses = losStatusEntity.getStatus();
            if (formStatuses.contains(screenName)) {
                // write in bottom to up because if top status will used first then it will always land on first page not the last page

                if (formStatuses.contains(ExternalCallStatusEnum.GST_ANALYTICS_DONE.getScreenName())) {
//                    return fetchFromTruthScreenDocDetails(ExternalCallStatusEnum.GST_ANALYTICS_DONE, GSTIN.getValue());
                }

                if (formStatuses.contains(ExternalCallStatusEnum.PAN_TO_GST_FETCHED.getScreenName())) {
                    return fetchFromTruthScreenDocDetails(ExternalCallStatusEnum.PAN_TO_GST_FETCHED, PAN_TO_GST.getValue());

                }

                if (formStatuses.contains(ExternalCallStatusEnum.PAN_COMPREHENSIVE_FETCHED.getScreenName())) {
                    return fetchFromTruthScreenDocDetails(ExternalCallStatusEnum.PAN_COMPREHENSIVE_FETCHED, PAN_COMPREHENSIVE.getValue());
                }
            }
        }
        return null;
    }
    private ScreenNameDTO fetchFromTruthScreenDocDetails(ExternalCallStatusEnum externalCallStatusEnum, int truthScreenDocTypeValue) throws Exception {
        TruthScreenDocDetails<?> truthScreenDocDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(),
                TruthScreenDocType.fromValue(truthScreenDocTypeValue));
            return convertIntoScreenNameDto(externalCallStatusEnum, truthScreenDocDetails);

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
        LosUserEntity losUserEntity = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
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

    public ScreenNameDTO saveDetailes(LosUserDTO updatedLosUserDTO) throws Exception {
        String screenName = updatedLosUserDTO.getScreenName();
        return switch (screenName) {
            case "BASIC_DETAILS" -> saveUserBasicDetails(updatedLosUserDTO);
            case "PERSONAL_DETAILS" -> saveUserPersonalDetails(updatedLosUserDTO);
            case "BUSINESS_DETAILS" -> saveUserBusinessDetails(updatedLosUserDTO);
            case "DOCUMENT_UPLOAD" -> updateDocumentAndReturnListOfDocument(updatedLosUserDTO);
            case "OFFER_GENERATION" -> saveGenerationDetailsAndReturnNextStageData(updatedLosUserDTO);
            default -> throw new IllegalStateException("Unexpected value: " + screenName);
        };
    }


    private ScreenNameDTO saveOfferAcceptanceDetailsAndReturnNextStageData(LosUserDTO updatedLosUserDTO) {
        return null;
    }

    private ScreenNameDTO saveGenerationDetailsAndReturnNextStageData(LosUserDTO updatedLosUserDTO) {
        return null;
    }

    private ScreenNameDTO updateDocumentAndReturnListOfDocument(LosUserDTO updatedLosUserDTO) {
        return null;
    }

    private ScreenNameDTO saveUserBusinessDetails(LosUserDTO updatedLosUserDTO) {
        return null;
    }

    private ScreenNameDTO saveUserPersonalDetails(LosUserDTO updatedLosUserDTO) throws Exception {
        LosUserEntity user = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
        if (user != null) {
            Address address = new Address();
            address.setCity(updatedLosUserDTO.getAddress().getCity());
            address.setState(updatedLosUserDTO.getAddress().getState());
            address.setZip(updatedLosUserDTO.getAddress().getPinCode());
            address.setLine1(updatedLosUserDTO.getAddress().getLine1());
            address.setLine2(updatedLosUserDTO.getAddress().getLine2());
            user.setAddress(address);

            user.setScreenName(ExternalCallStatusEnum.PAN_TO_GST_FETCHED.getScreenName());
            losUserRepository.save(user);
            LosStatusEntity losStatusEntity = losStatusEntityRepository.findByLosUserEntityId(user.getId());

            return getScreenStatus(losStatusEntity, user.getScreenName());
        }
        throw new CustomException("No data found");
    }

//    private ScreenNameDTO getScreenData(LosUserEntity user, LosUserDTO updatedLosUserDTO, LosScreenStatusEnum currentScreenStatusEnum) {
//            LosStatusEntity losStatusEntity = losStatusEntityRepository.findByLosUserEntityId(user.getId());
//            if (losStatusEntity != null) {
//                if (user.getScreenName().equals(LosScreenStatusEnum.DOCUMENT_UPLOAD.name())) {
//                    return convertIntoScreenNameDto(LosScreenStatusEnum.DOCUMENT_UPLOAD,
//                            kycDocDetailsManager.findKycDocDetailsByTenantId(RequestData.getTenantId()));
//                }
//                return getScreenStatus(losStatusEntity, token, user);
//            }
//
//        return null;
//    }

    private ScreenNameDTO saveUserBasicDetails(LosUserDTO updatedLosUserDTO) throws Exception {
        LosUserEntity user = losUserRepository.findBySrCompanyId(RequestData.getTenantId());
        if (user == null) {
            user = LosUserEntity.builder()
                    .srCompanyId(RequestData.getTenantId())
                    .userId(String.valueOf(RequestData.getUserId()))
                    .isMobileVerified(false)
                    .build();
        }
        user.setEmail(updatedLosUserDTO.getEmail());
        user.setTypeOfEntity(updatedLosUserDTO.getTypeOfEntity());
        user.setPan(updatedLosUserDTO.getPan());
        user.setLoanAmount(updatedLosUserDTO.getLoanAmount());
        user.setFirstName(updatedLosUserDTO.getFirstName());
        user.setLastName(updatedLosUserDTO.getLastName());
        user.setReferenceId(sequenceGeneratorService.generateSequence(REFERENCE_ID_SEQUENCE));
        if (!user.getMobile().equals(updatedLosUserDTO.getMobile())) {
            user.setMobile(updatedLosUserDTO.getMobile());
        }



        user.setScreenName(ExternalCallStatusEnum.PAN_COMPREHENSIVE_FETCHED.getScreenName());

        LosUserEntity userEntity = losUserRepository.save(user);
        IdSearchResponseDto<?> idSearchResponseDto = sendRequest(PAN_COMPREHENSIVE.getValue(), updatedLosUserDTO.getPan(), userEntity.getId(),
                ExternalCallStatusEnum.PAN_COMPREHENSIVE_FETCHED.name());


        kafkaMessagePublisherUtil.publishMessage(appProperties.getCapitalTopicName()
                , kafkaMessagePublisherUtil.getKafkaMessage(userEntity.getId(),
                        KafkaEventTypes.LOS_EXTERNAL_CALL_EVENT.name(), null, RequestData.getCorrelationId(), null));


        return convertIntoScreenNameDto(PAN_COMPREHENSIVE_FETCHED, idSearchResponseDto);
    }

    private IdSearchResponseDto<?> sendRequest(int docType, String docValue, String userId, String changeStatusTo) throws Exception {
        IdSearchResponseDto<?> idSearchResponseDto = idService.sendRequest(new IdSearchRequestDto(RequestData.getTenantId() + HashUtil.generateRandomId(), docType, docValue, null, null, null));
        updateDetailsIntoEventStatusHandlerTable(userId, changeStatusTo);
        return idSearchResponseDto;
    }

    @Override
    public void updateDetailsIntoEventStatusHandlerTable(String userEntityId, String statusName) {
        LosStatusEntity losStatusEntity = losStatusEntityRepository.findByLosUserEntityId(userEntityId);

        if (losStatusEntity != null) {
            Set<String> status = losStatusEntity.getStatus();
            status.add(statusName);
            losStatusEntity.setStatus(status);
        } else {
            losStatusEntity = LosStatusEntity.builder()
                    .losUserEntityId(userEntityId)
                    .status(Set.of(statusName))
                    .build();
        }
        losStatusEntityRepository.save(losStatusEntity);
    }
}
