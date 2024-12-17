package com.sr.capital.external.crif.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.dto.RequestData;
import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.dto.response.CrifUserDetailsResponseDto;
import com.sr.capital.external.crif.util.CrifUserModelHelper;
import com.sr.capital.external.crif.util.CrifVerificationUtils;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import com.sr.capital.service.UserService;
import com.sr.capital.util.MapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static com.sr.capital.external.crif.Constant.Constant.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrifOtpServiceImpl implements CrifOtpService {

    private final CrifUserModelRepo crifUserModelRepo;
    private final CrifUserModelHelper crifUserModelHelper;
    private final CrifVerificationUtils crifVerificationUtils;
    private final CrifPartnerService crifPartnerService;
    private final UserService userService;
    private final ObjectMapper mapper;;

    @Override
    public CrifResponse generateOtp(CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws IOException {
        CrifResponse crifResponse  = CrifResponse.builder().build();

        Optional<CrifUserModel> optional = crifUserModelHelper.findByMobile(crifGenerateOtpRequestModel.getMobile());
        CrifUserModel crifUserModel;
        crifUserModel = optional.orElseGet(() -> getCrifUserModelInstance(crifGenerateOtpRequestModel));
        VerificationOrchestratorRequest verificationOrchestratorRequest = null;

        if (!crifUserModel.getIsOtpVerified()) {
            try {
                verificationOrchestratorRequest = crifVerificationUtils.sendOtp(crifUserModel);
                crifUserModel.setVerificationToken(verificationOrchestratorRequest.getVerificationEntity().getId());
                crifUserModelRepo.save(crifUserModel);
            } catch (CustomException e) {
                throw new RuntimeException("Error while sending the otp {} ", e);
            }
        } else {
            CrifVerifyOtpRequestModels crifVerifyOtpRequestModels = MapperUtils.convertValue(crifGenerateOtpRequestModel,
                    CrifVerifyOtpRequestModels.class);
            Map<String, Object> res = crifPartnerService.initiateBureauAndGetQuestionnaire(crifVerifyOtpRequestModels);
            setResponse(crifResponse, res);
            return crifResponse;
        }

        if (verificationOrchestratorRequest != null && verificationOrchestratorRequest.getVerificationEntity() != null) {
            crifResponse.setToken(verificationOrchestratorRequest.getVerificationEntity().getId());
            crifResponse.setStatus(Constant.OTP_VERIFICATION_PENDING);
        }

        return crifResponse;
    }

    private CrifUserModel getCrifUserModelInstance(CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) {
        CrifUserModel.CrifUserModelBuilder builder = CrifUserModel.builder()
                .firstName(crifGenerateOtpRequestModel.getFirstName())
                .lastName(crifGenerateOtpRequestModel.getLastName())
                .email(crifGenerateOtpRequestModel.getEmail())
                .documentType(crifGenerateOtpRequestModel.getDocType())
                .documentValue(crifGenerateOtpRequestModel.getDocValue())
                .mobile(crifGenerateOtpRequestModel.getMobile())
                .srCompanyId(RequestData.getTenantId())
                .isOtpVerified(false);

        if (crifGenerateOtpRequestModel.getUtmSource() != null) {
            builder.utmSource(crifGenerateOtpRequestModel.getUtmSource());
        }
        if (crifGenerateOtpRequestModel.getUtmMedium() != null) {
            builder.utmMedium(crifGenerateOtpRequestModel.getUtmMedium());
        }
        if (crifGenerateOtpRequestModel.getUtmCampaign() != null) {
            builder.utmCampaign(crifGenerateOtpRequestModel.getUtmCampaign());
        }
        if (crifGenerateOtpRequestModel.getUtmTerm() != null) {
            builder.utmTerm(crifGenerateOtpRequestModel.getUtmTerm());
        }
        if (crifGenerateOtpRequestModel.getUtmContent() != null) {
            builder.utmContent(crifGenerateOtpRequestModel.getUtmContent());
        }
        return builder.build();
    }

    private void setResponse(CrifResponse crifResponse, Map<String, Object> data) {
        if (data != null && !data.isEmpty() && data.containsKey(STAGE)) {
            switch (String.valueOf(data.get(STAGE))) {
                case STAGE_2 -> crifResponse.setQuestionnaireResponse(data.get(DATA));
                case STAGE_3 -> crifResponse.setReport(data.get(DATA));
            }
            crifResponse.setStatus(String.valueOf(data.get(STAGE)));
        }
    }

    @Override
    public CrifResponse verifyOtp(@Valid CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException {
        CrifResponse crifResponse = CrifResponse.builder().build();

        Optional<CrifUserModel> optional = crifUserModelHelper.findByMobile(crifGenerateOtpRequestModel.getMobile());
        if (optional.isPresent()) {
            CrifUserModel crifUserModel = optional.get();
            if (crifUserModel.getVerificationToken().equals(crifGenerateOtpRequestModel.getVerificationToken())) {

                VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
                verifyOtpRequest.setVerificationToken(crifGenerateOtpRequestModel.getVerificationToken());
                verifyOtpRequest.setOtp(crifGenerateOtpRequestModel.getOtp());
                Boolean isVerified = crifVerificationUtils.verifyOtp(verifyOtpRequest);

                if (isVerified != null && isVerified) {
                    crifUserModel.setIsOtpVerified(true);
                    crifUserModelRepo.save(crifUserModel);

                    Map<String, Object> map = crifPartnerService.initiateBureauAndGetQuestionnaire(crifGenerateOtpRequestModel);

                    setResponse(crifResponse, map);

                    return crifResponse;
                }
            } else {
                crifResponse.setStatus("Otp validation failed");
            }
        } else {
            throw new CustomException("User not found");
        }
        return crifResponse;
    }

    @Override
    public void updateOtpStatus(String mobile) {
        Optional<CrifUserModel> optional = crifUserModelHelper.findByMobile(mobile);
        if (optional.isPresent()) {
            optional.get().setIsOtpVerified(false);
            crifUserModelRepo.save(optional.get());
        }
    }

    @Override
    public CrifUserDetailsResponseDto getUserDetails(String token) {
        CrifUserModel crifUserModel = crifUserModelRepo.findByCreatedBy(String.valueOf(RequestData.getUserId()));
        if (crifUserModel != null) {
            return CrifUserDetailsResponseDto.builder().userId(crifUserModel.getSrCompanyId()).email(crifUserModel.getEmail())
                    .mobile(crifUserModel.getMobile()).lastName(crifUserModel.getLastName()).firstName(crifUserModel.getFirstName())
                    .documentType(crifUserModel.getDocumentType()).documentValue(crifUserModel.getDocumentValue()).build();
        } else {
            InternalTokenUserDetailsResponse userDetailsUsingInternalToken = userService.getUserDetailsUsingInternalToken(token);
            if (userDetailsUsingInternalToken != null) {
                return CrifUserDetailsResponseDto.builder().userId(userDetailsUsingInternalToken.getUserId()).email(userDetailsUsingInternalToken.getEmail())
                        .mobile(userDetailsUsingInternalToken.getMobile()).lastName(userDetailsUsingInternalToken.getLastName()).firstName(userDetailsUsingInternalToken.getFirstName())
                        .build();
            }
        }
        return null;
    }
}
