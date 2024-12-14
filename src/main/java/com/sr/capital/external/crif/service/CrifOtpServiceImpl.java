package com.sr.capital.external.crif.service;


import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.util.CrifVerificationUtils;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import com.sr.capital.util.MapperUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CrifVerificationUtils crifVerificationUtils;
    private final CrifPartnerService crifPartnerService;

    @Override
    public CrifResponse generateOtp(CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws IOException {
        CrifResponse crifResponse  = CrifResponse.builder().build();

        Optional<CrifUserModel> optional = crifUserModelRepo.findByMobile(crifGenerateOtpRequestModel.getMobile());
        CrifUserModel crifUserModel;
        crifUserModel = optional.orElseGet(() -> CrifUserModel.builder()
                .firstName(crifGenerateOtpRequestModel.getFirstName())
                .lastName(crifGenerateOtpRequestModel.getLastName())
                .email(crifGenerateOtpRequestModel.getEmail())
                .documentType(crifGenerateOtpRequestModel.getDocType())
                .documentValue(crifGenerateOtpRequestModel.getDocValue())
                .mobile(crifGenerateOtpRequestModel.getMobile())
                .isOtpVerified(false)
                .build());
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
    public CrifResponse verifyOtp(@Valid CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) {
        CrifResponse crifResponse = CrifResponse.builder().build();

        Optional<CrifUserModel> optional = crifUserModelRepo.findByMobile(crifGenerateOtpRequestModel.getMobile());
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
        }
        return crifResponse;
    }

    @Override
    public void updateOtpStatus(String mobile) {
        Optional<CrifUserModel> optional = crifUserModelRepo.findByMobile(mobile);
        if (optional.isPresent()) {
            optional.get().setIsOtpVerified(false);
            crifUserModelRepo.save(optional.get());
        }
    }
}
