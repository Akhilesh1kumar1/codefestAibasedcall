package com.sr.capital.external.truthscreen.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.exception.custom.RequestTransformerNotFoundException;
import com.sr.capital.external.truthscreen.adapter.TruthScreenAdapter;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.enums.TruthScreenStatus;
import com.sr.capital.external.truthscreen.repository.PanRepository;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocDetailsRepository;
import com.sr.capital.external.truthscreen.service.PanService;

import com.sr.capital.external.truthscreen.service.strategy.TruthScreenEntityConstructorStrategy;
import com.sr.capital.external.truthscreen.service.transformers.TruthScreenExternalRequestTransformerStrategy;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PanServiceImpl implements PanService {


    private final TruthScreenAdapter truthScreenAdapter;

    private final PanRepository panRepository;

    private final TruthScreenExternalRequestTransformerStrategy externalRequestTransformerStrategy;

    private final TruthScreenDocDetailsRepository truthScreenDocDetailsRepository;

    @Autowired
    private TruthScreenEntityConstructorStrategy truthEntityConstructorStrategy;


    @Override
    public T sendPanRequest(IdSearchRequestDto requestDTO) throws RequestTransformerNotFoundException {
        TruthScreenDocDetails<?> truthScreenDocDetails = truthScreenDocDetailsRepository.findBySrCompanyIdAndTruthScreenDocType(RequestData.getTenantId(), TruthScreenDocType.fromValue(requestDTO.getDocType()));
        if (truthScreenDocDetails == null){

            TruthScreenDocOrchestratorRequest truthScreenDocOrchestratorRequest = new TruthScreenDocOrchestratorRequest();
            truthScreenDocOrchestratorRequest.setDocType(TruthScreenDocType.fromValue(requestDTO.getDocType()));
            truthScreenDocOrchestratorRequest.setDocNumber(requestDTO.getDocNumber());
            truthScreenDocOrchestratorRequest.setSrCompanyId(RequestData.getTenantId());
            TruthScreenBaseRequest<?> truthScreenBaseRequest = externalRequestTransformerStrategy.transformExtractionRequest(truthScreenDocOrchestratorRequest);
            truthScreenDocOrchestratorRequest.setTruthScreenBaseRequest(truthScreenBaseRequest);

            try {
                TruthScreenBaseResponse<?> truthScreenBaseResponse = truthScreenAdapter.extractDetails(truthScreenBaseRequest);
                truthScreenDocOrchestratorRequest.setTruthScreenBaseResponse(truthScreenBaseResponse);
                TruthScreenDocDetails<T> truthScreenDocDetailsToBeSaved = truthEntityConstructorStrategy.constructEntity(truthScreenDocOrchestratorRequest,truthScreenDocOrchestratorRequest.getTruthScreenDocDetails(),getResponseClass(TruthScreenDocType.valueOf(String.valueOf(requestDTO.getDocType()))));
                truthScreenDocDetailsRepository.save(truthScreenDocDetailsToBeSaved);
                return truthScreenDocDetailsToBeSaved.getDetails();
                //return truthScreenBaseResponse.get();

                //response should be that which is saved in the db not the 3rd party api

            } catch (Exception e) {
                throw new RuntimeException(e + "error occured while try to get response from the adapter");
            }


        }
        else if(truthScreenDocDetails !=null && truthScreenDocDetails.getInitialStatus() == TruthScreenStatus.VERIFIED ){
            //return the details
            PanDetails panDetails = panRepository.findByDocNumber(requestDTO.getDocNumber());
            //mapping the panDEtails with panResponseDto
            Map<String, Object> map = new HashMap<>();
            return null;//PanResponseDto.builder().status(1).msg(map).build();

        }
        else if (truthScreenDocDetails != null && truthScreenDocDetails.getInitialStatus() == TruthScreenStatus.UNVERIFIED ){
            //pick transID
            //pick ts_trans_id
            //call 2nd API
            // if status == 1 and state == Compelte --> add in main db and update panDB
            // else update in transDB
            return null;
        }
        return null;

    }

    private Class<?> getResponseClass(TruthScreenDocType docType) throws CustomException {
        switch(docType){
            case PAN:
                return PanDetails.class;
            default:
                throw new CustomException("Invalid Doc type");
        }
    }

        @Override
    public AsyncReponseDto getAsyncStatus(AsyncRequestDto asyncRequestDto) {
        return null;
    }
}
