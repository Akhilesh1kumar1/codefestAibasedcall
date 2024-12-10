package com.sr.capital.external.crif.service;


import com.sr.capital.dto.request.VerificationOrchestratorRequest;
import com.sr.capital.dto.request.VerifyOtpRequest;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.dto.request.CrifGenerateOtpRequestModel;
import com.sr.capital.external.crif.dto.request.CrifVerifyOtpRequestModels;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.util.CrifVerificationUtils;
import com.sr.capital.repository.mongo.CrifUserModelRepo;
import com.sr.capital.util.MapperUtils;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.sr.capital.external.crif.Constant.Constant.*;

@Service
@Slf4j
public class CrifOtpServiceImpl implements CrifOtpService {

    private final CrifUserModelRepo crifUserModelRepo;
    private final CrifVerificationUtils crifVerificationUtils;
    private final CrifPartnerService crifPartnerService;

    public CrifOtpServiceImpl(CrifUserModelRepo crifUserModelRepo, CrifVerificationUtils crifVerificationUtils, CrifPartnerService crifPartnerService) {
        this.crifUserModelRepo = crifUserModelRepo;
        this.crifVerificationUtils = crifVerificationUtils;
        this.crifPartnerService = crifPartnerService;
    }

    @Override
    public CrifResponse generateOtp(CrifGenerateOtpRequestModel crifGenerateOtpRequestModel) throws IOException {
        CrifResponse crifResponse  = CrifResponse.builder().build();
        Optional<CrifUserModel> optional = crifUserModelRepo.findByMobile(crifGenerateOtpRequestModel.getMobile());
        CrifUserModel crifUserModel;
        if (optional.isPresent()) {
            crifUserModel = optional.get();
        } else {
            crifUserModel = CrifUserModel.builder()
                    .firstName(crifGenerateOtpRequestModel.getFirstName())
                    .lastName(crifGenerateOtpRequestModel.getLastName())
                    .email(crifGenerateOtpRequestModel.getEmail())
                    .documentType(crifGenerateOtpRequestModel.getDocType())
                    .documentValue(crifGenerateOtpRequestModel.getDocValue())
                    .mobile(crifGenerateOtpRequestModel.getMobile())
                    .isOtpVerified(false)
                    .build();
            crifUserModelRepo.save(crifUserModel);
        }
        VerificationOrchestratorRequest verificationOrchestratorRequest = null;

        if (!crifUserModel.getIsOtpVerified()) {
            try {
                verificationOrchestratorRequest = crifVerificationUtils.sendOtp(crifUserModel);
            } catch (CustomException e) {
                throw new RuntimeException("Error while sending the otp {} ", e);
            }
        } else {
            CrifVerifyOtpRequestModels crifVerifyOtpRequestModels = MapperUtils.convertValue(crifGenerateOtpRequestModel,
                    CrifVerifyOtpRequestModels.class);
            Map<String, Object> res = crifPartnerService.initiateBureau(crifVerifyOtpRequestModels);
            setResponse(crifResponse, res);
            return crifResponse;
        }

        if (verificationOrchestratorRequest != null && verificationOrchestratorRequest.getVerificationEntity() != null) {
            crifResponse.setToken(verificationOrchestratorRequest.getVerificationEntity().getId());
        }

        return crifResponse;
    }

    private void setResponse(CrifResponse crifResponse, Map<String, Object> data) {
        if (data != null && !data.isEmpty() && data.containsKey(STAGE)) {
            switch (String.valueOf(data.get(STAGE))) {
                case STAGE_2 -> crifResponse.setQuestionnaireResponse(data.get(DATA));
                case STAGE_3 -> crifResponse.setReport(data.get(DATA));
            }
        }
    }

    @Override
    public CrifResponse verifyOtp(@Valid CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) {
        CrifResponse crifResponse = CrifResponse.builder().build();

        Optional<CrifUserModel> optional = crifUserModelRepo.findByMobile(crifGenerateOtpRequestModel.getMobile());
        if (optional.isPresent()) {
            VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
            verifyOtpRequest.setVerificationToken(crifGenerateOtpRequestModel.getVerificationToken());
            verifyOtpRequest.setOtp(crifGenerateOtpRequestModel.getOtp());
            Boolean isVerified = crifVerificationUtils.verifyOtp(verifyOtpRequest);

            if (isVerified != null && isVerified) {
                optional.get().setIsOtpVerified(true);
                crifUserModelRepo.save(optional.get());

                Map<String, Object> map = crifPartnerService.initiateBureau(crifGenerateOtpRequestModel);

                setResponse(crifResponse, map);

                return crifResponse;
            }
        }
        return crifResponse;
    }
}
