package com.sr.capital.external.truthscreen.service.transformers;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.external.truthscreen.dto.data.NDDExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanComprehensiveDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.NddUserType;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.extractions.TruthScreenNddExtractionRequest;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.service.transformers.interfaces.TruthScreenExternalRequestTransformer;
import com.sr.capital.util.HashUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@Service
@RequiredArgsConstructor
public class TruthScreenNDDExtractionRequestTransformer implements TruthScreenExternalRequestTransformer {

    private final AppProperties appProperties;

    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    private final AES256 aes256;

    @Override
    public <T extends TruthScreenBaseRequest<?>> T transformRequest(TruthScreenDocOrchestratorRequest request) throws NoSuchAlgorithmException, NoSuchProviderException {
        PanComprehensiveDetails panComprehensiveDetails = getPanComprehensiveDetails(request);

        NddUserType userTypeEnum = "company".equals(aes256.decrypt(panComprehensiveDetails.getData().getCategory()))
                ? NddUserType.ENTITY
                : NddUserType.INDIVIDUAL;

        NDDExtractionRequestData nddExtractionRequestData = NDDExtractionRequestData.builder()
                .transId(Integer.parseInt(request.getSrCompanyId() + HashUtil.generateRandomIntegerUserId()))
                .docType(TruthScreenDocType.NDD.getValue())
                .name(aes256.decrypt(panComprehensiveDetails.getData().getFullName()))
                .threshold(appProperties.getAuthBridgeNddThreshold())
                .userType(userTypeEnum.getDisplayName())
                .allDatabase(Integer.parseInt(appProperties.getAuthBridgeNddAllDatabase()))
                .databaseList("") //doing this because we need to give this value
                .build();
        request.setTransId(String.valueOf(nddExtractionRequestData.getTransId()));
        return (T) TruthScreenNddExtractionRequest.builder().details(nddExtractionRequestData).build();
    }

    private PanComprehensiveDetails getPanComprehensiveDetails(TruthScreenDocOrchestratorRequest request){
        TruthScreenDocDetails<?> docDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(request.getSrCompanyId(),TruthScreenDocType.PAN_COMPREHENSIVE);
        PanComprehensiveDetails panComprehensiveDetails = (PanComprehensiveDetails) docDetails.getDetails();
        return panComprehensiveDetails;
    }

}
