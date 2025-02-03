package com.sr.capital.external.truthscreen.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.adapter.TruthScreenAdapter;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.entity.*;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.manager.TruthScreenDocDetailsManager;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.service.IdService;
import com.sr.capital.external.truthscreen.service.strategy.TruthScreenEntityConstructorStrategy;
import com.sr.capital.external.truthscreen.service.strategy.TruthScreenIdSearchResponseStrategy;
import com.sr.capital.external.truthscreen.service.strategy.TruthScreenRequestValidatorStrategy;
import com.sr.capital.external.truthscreen.service.transformers.TruthScreenExternalRequestTransformerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class IdServiceImpl implements IdService {

    private final TruthScreenAdapter truthScreenAdapter;

    private final TruthScreenExternalRequestTransformerStrategy externalRequestTransformerStrategy;

    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    private final TruthScreenIdSearchResponseStrategy truthScreenIdSearchResponseStrategy;

    private final TruthScreenEntityConstructorStrategy truthEntityConstructorStrategy;

    private final TruthScreenRequestValidatorStrategy truthScreenRequestValidatorStrategy;

    private final TruthScreenDocDetailsManager truthScreenDocDetailsManager;

    @Override
    public IdSearchResponseDto<?> sendRequest(IdSearchRequestDto requestDTO) throws Exception {
        TruthScreenDocDetails<?> truthScreenDocDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(), TruthScreenDocType.fromValue(requestDTO.getDocType()));
        TruthScreenDocOrchestratorRequest truthScreenDocOrchestratorRequest = new TruthScreenDocOrchestratorRequest();
        //truthScreenDocOrchestratorRequest = truthScreenRequestValidatorStrategy.validateRequest(truthScreenDocOrchestratorRequest, truthScreenDocDetails, TruthScreenDocType.fromValue(requestDTO.getDocType()));
        if (truthScreenDocDetails == null) {
            return processNewRequest(requestDTO, truthScreenDocOrchestratorRequest);
        } else {
            return processExistingRequest(truthScreenDocDetails, requestDTO);
        }
    }

    private IdSearchResponseDto<?> processNewRequest(IdSearchRequestDto requestDTO, TruthScreenDocOrchestratorRequest truthScreenDocOrchestratorRequest) throws RequestTransformerNotFoundException {
        truthScreenDocOrchestratorRequest.setDocType(TruthScreenDocType.fromValue(requestDTO.getDocType()));
        truthScreenDocOrchestratorRequest.setDocNumber(requestDTO.getDocNumber());
        truthScreenDocOrchestratorRequest.setSrCompanyId(RequestData.getTenantId());
        TruthScreenBaseRequest<?> truthScreenBaseRequest = externalRequestTransformerStrategy.transformExtractionRequest(truthScreenDocOrchestratorRequest);
        truthScreenDocOrchestratorRequest.setTruthScreenBaseRequest(truthScreenBaseRequest);

        try {

            TruthScreenBaseResponse<?> truthScreenBaseResponse = truthScreenAdapter.extractDetails(truthScreenBaseRequest);
            truthScreenDocOrchestratorRequest.setTruthScreenBaseResponse(truthScreenBaseResponse);
            TruthScreenDocDetails<?> truthScreenDocDetailsToBeSaved = truthEntityConstructorStrategy.constructEntity(
                    truthScreenDocOrchestratorRequest,
                    truthScreenDocOrchestratorRequest.getTruthScreenDocDetails(),
                    getResponseClass(TruthScreenDocType.fromValue(requestDTO.getDocType()))
            );
            truthScreenDocDetailsManager.saveDocs(truthScreenDocDetailsToBeSaved);
            return truthScreenIdSearchResponseStrategy.constructResponse(
                    truthScreenDocDetailsToBeSaved,
                    getResponseClass(TruthScreenDocType.fromValue(requestDTO.getDocType()))
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting response from the adapter: " + e);
        }
    }

    private IdSearchResponseDto<?> processExistingRequest(TruthScreenDocDetails<?> truthScreenDocDetails, IdSearchRequestDto requestDTO) {
        try {
            return truthScreenIdSearchResponseStrategy.constructResponse(
                    truthScreenDocDetails,
                    getResponseClass(TruthScreenDocType.fromValue(requestDTO.getDocType()))
            );
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while fetching data from the database: " + e);
        }
    }

    private Class<?> getResponseClass(TruthScreenDocType docType) throws CustomException {
        switch (docType) {
            case PAN:
                return PanDetails.class;
            case PAN_COMPREHENSIVE:
                return PanComprehensiveDetails.class;
            case PAN_COMPLIANCE:
                return PanComplianceDetails.class;
            case PAN_TO_GST:
                return PanToGstDetails.class;
            case GSTIN:
                return GSTinDetails.class;
            case CIN:
                return CinDetails.class;
            default:
                throw new CustomException("Invalid Doc type");
        }
    }
}
