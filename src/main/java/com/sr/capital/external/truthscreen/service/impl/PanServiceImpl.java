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
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.service.PanService;
import com.sr.capital.external.truthscreen.service.strategy.TruthScreenEntityConstructorStrategy;
import com.sr.capital.external.truthscreen.service.strategy.TruthScreenIdSearchResponseStrategy;
import com.sr.capital.external.truthscreen.service.transformers.TruthScreenExternalRequestTransformerStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PanServiceImpl implements PanService {

    private final TruthScreenAdapter truthScreenAdapter;

    private final TruthScreenExternalRequestTransformerStrategy externalRequestTransformerStrategy;

    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    private final TruthScreenIdSearchResponseStrategy truthScreenIdSearchResponseStrategy;

    private final TruthScreenEntityConstructorStrategy truthEntityConstructorStrategy;

    @Override
    public IdSearchResponseDto<?> sendPanRequest(IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        TruthScreenDocDetails<?> truthScreenDocDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(), TruthScreenDocType.fromValue(requestDTO.getDocType()));

        if (truthScreenDocDetails == null) {
            return processNewRequest(requestDTO);
        } else {
            return processExistingRequest(truthScreenDocDetails, requestDTO);
        }
    }

    private IdSearchResponseDto<?> processNewRequest(IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        TruthScreenDocOrchestratorRequest truthScreenDocOrchestratorRequest = new TruthScreenDocOrchestratorRequest();
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

            truthScreenDocDetailsRepository.save(truthScreenDocDetailsToBeSaved);

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
            default:
                throw new CustomException("Invalid Doc type");
        }
    }
}
