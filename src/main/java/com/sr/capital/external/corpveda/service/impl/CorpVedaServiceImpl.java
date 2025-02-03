package com.sr.capital.external.corpveda.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.corpveda.docs.CorpVedaDocDetails;
import com.sr.capital.external.corpveda.docs.CorpVedaDocOrChestratorRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaBaseRequest;
import com.sr.capital.external.corpveda.dto.request.CorpVedaRequestDto;
import com.sr.capital.external.corpveda.dto.response.CorpVedaBaseResponse;
import com.sr.capital.external.corpveda.dto.response.CorpVedaResponseDto;
import com.sr.capital.external.corpveda.entity.PartnerDetailsMetaData;

import com.sr.capital.external.corpveda.entity.PartnerFreeDataDetails;
import com.sr.capital.external.corpveda.enums.CorpVedaDocType;
import com.sr.capital.external.corpveda.repository.CorpVedaDocDetailsRepository;
import com.sr.capital.external.corpveda.repository.PartnerDetailsMetaDataRepository;
import com.sr.capital.external.corpveda.service.CorpVedaService;
import com.sr.capital.external.corpveda.service.strategy.CorpVedaEntityConstructorStrategy;
import com.sr.capital.external.corpveda.service.strategy.CorpVedaRequestTransformerStrategy;
import com.sr.capital.external.corpveda.adapter.CorpVedaAdapter;
import com.sr.capital.external.corpveda.service.strategy.CorpVedaResponseEntityCOnstructorStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CorpVedaServiceImpl implements CorpVedaService {

    private final CorpVedaDocDetailsRepository corpVedaDocDetailsRepository;

    private final CorpVedaAdapter corpVedaAdapter;

    private final CorpVedaRequestTransformerStrategy corpVedaRequestTransformerStrategy;

    private final PartnerDetailsMetaDataRepository partnerDetailsMetaDataRepository;

    private final CorpVedaResponseEntityCOnstructorStrategy corpVedaResponseEntityConstructorStrategy;

    private final CorpVedaEntityConstructorStrategy corpVedaEntityConstructorStrategy;
    @Override
    public CorpVedaResponseDto getCinDetails(CorpVedaRequestDto requestDto) throws Exception {
        CorpVedaDocDetails<?> corpVedaDocDetails = corpVedaDocDetailsRepository.findBySrCompanyIdAndCorpVedaDocType(RequestData.getTenantId(), CorpVedaDocType.fromValue(requestDto.getDocType()));
        if (corpVedaDocDetails == null){
            return processNewRequest(requestDto);
        } else {
            return processExistingRequest(corpVedaDocDetails, requestDto);
        }
    }

    public CorpVedaResponseDto processNewRequest(CorpVedaRequestDto requestDto) throws Exception {
        CorpVedaDocOrChestratorRequest request = new CorpVedaDocOrChestratorRequest();
        request.setDocType(CorpVedaDocType.fromValue(requestDto.getDocType()));
        request.setDocNumber(requestDto.getDocValue());
        request.setSrCompanyId(RequestData.getTenantId());
        CorpVedaBaseRequest<?> baseRequest = corpVedaRequestTransformerStrategy.transformRequest(request);
        request.setCorpVedaBaseRequest(baseRequest);


        try {
            CorpVedaBaseResponse<?> corpVedaBaseResponse = corpVedaAdapter.extractDetails(request);
            request.setCOrpVedaBaseResponse(corpVedaBaseResponse);
            CorpVedaDocDetails<?> corpVedaDocDetails = corpVedaEntityConstructorStrategy.constructEntity(request, request.getCorpVedaDocDetails(), Objects.requireNonNull(getResponseClass(CorpVedaDocType.fromValue(requestDto.getDocType()))));
            //PartnerDetailsMetaData metaData = partnerDetailsMetaDataRepository.findBy(RequestData.getTenantId());
            if (corpVedaDocDetails.getCorpVedaDocType().equals(CorpVedaDocType.CIN_SEARCH_PLACE_ORDER)){
                corpVedaDocDetailsRepository.save(corpVedaDocDetails);
            }
            return buildResponse(corpVedaDocDetails);
        }
        catch (Exception e) {
            throw new RuntimeException("Error while processing new request");
        }
    }

    private CorpVedaResponseDto processExistingRequest(CorpVedaDocDetails<?> truthScreenDocDetails, CorpVedaRequestDto requestDto) {
        return null;
    }

    private CorpVedaResponseDto buildResponse(CorpVedaDocDetails<?> corpVedaDocDetails) {
        if (corpVedaDocDetails.getDetails() instanceof PartnerFreeDataDetails){
            PartnerFreeDataDetails placeOrderServiceDetails = (PartnerFreeDataDetails) corpVedaDocDetails.getDetails();
            return CorpVedaResponseDto.builder()
                    .srCompanyId(placeOrderServiceDetails.getSrCompanyId())
                    .cin(placeOrderServiceDetails.getData().getBasicDetails().getCin())
                    .initialStatus("Pending")
                    .email(placeOrderServiceDetails.getData().getContactDetails().getEmail())
                    .authorizedCapital(placeOrderServiceDetails.getData().getBasicDetails().getAuthorisedCapital())
                    .companyName(placeOrderServiceDetails.getData().getBasicDetails().getCompanyName())
                    .companyPan(placeOrderServiceDetails.getData().getBasicDetails().getCompanyPan())
                    .registrationNumber(placeOrderServiceDetails.getData().getBasicDetails().getRegistrationNo())
                    .build();
        }
        else {
            return CorpVedaResponseDto.builder()
                    .cin("NA")
                    .initialStatus("Pending")
                    .email("NA")
                    .authorizedCapital("NA")
                    .companyName("NA")
                    .companyPan("NA")
                    .registrationNumber("NA")
                    .build();
        }
    }


    private Class<?> getResponseClass(CorpVedaDocType docType) {
        switch (docType) {
            case CIN_SEARCH_PLACE_ORDER:
                return PartnerFreeDataDetails.class;
            case CIN_SEARCH_GET_DATA:
                return PartnerDetailsMetaData.class;
            default:
                return null;
        }
    }
}
