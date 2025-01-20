package com.sr.capital.external.corpveda.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaRequestDto;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.corpveda.dto.response.CorpVedaResponseDto;
import com.sr.capital.external.corpveda.repository.CorpVedaDocDetailsRepository;
import com.sr.capital.external.corpveda.service.CorpVedaService;
import com.sr.capital.external.corpveda.service.strategy.CorpVedaRequestTransformerStrategy;
import com.sr.capital.external.truthscreen.adapter.CorpVedaAdapter;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CorpVedaServiceImpl implements CorpVedaService {

    private final CorpVedaDocDetailsRepository corpVedaDocDetailsRepository;

    private final CorpVedaAdapter corpVedaAdapter;

    private final CorpVedaRequestTransformerStrategy corpVedaRequestTransformerStrategy;

    @Override
    public CorpVedaResponseDto getCinDetails(CorpVedaRequestDto requestDto) throws Exception {
        CorpVedaDocDetails<?> corpVedaDocDetails = corpVedaDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(), TruthScreenDocType.CIN_SEARCH);
        if (corpVedaDocDetails == null){
            return processNewRequest(requestDto);
        } else {
            return processExistingRequest(corpVedaDocDetails, requestDto);
        }
    }

    private CorpVedaResponseDto processNewRequest(CorpVedaRequestDto requestDto) throws RequestTransformerNotFoundException {
        CorpVedaDocOrChestratorRequest request = new CorpVedaDocOrChestratorRequest();
        request.setDocType(TruthScreenDocType.CIN_SEARCH);
        request.setDocNumber(requestDto.getCin());
        CorpVedaBaseRequest<?> baseRequest = corpVedaRequestTransformerStrategy.transformRequest(request);
        request.setCorpVedaBaseRequest(baseRequest);


        try {
            CorpVedaBaseResponse<?> corpVedaBaseResponse = corpVedaAdapter.extractDetails(request);
            request.setCOrpVedaBaseResponse(corpVedaBaseResponse);
        }
        catch (Exception e) {
            throw new RuntimeException("Error while processing new request");
        }
        return null;
    }

    private CorpVedaResponseDto processExistingRequest(CorpVedaDocDetails<?> truthScreenDocDetails, CorpVedaRequestDto requestDto) {
        return null;
    }
}
