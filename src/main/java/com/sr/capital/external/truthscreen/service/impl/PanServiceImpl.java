package com.sr.capital.external.truthscreen.service.impl;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.adapter.TruthScreenAdapter;
import com.sr.capital.external.truthscreen.dto.request.AsyncRequestDto;
import com.sr.capital.external.truthscreen.dto.request.IdSearchRequestDto;
import com.sr.capital.external.truthscreen.dto.response.AsyncReponseDto;
import com.sr.capital.external.truthscreen.dto.response.PanResponseDto;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.enums.TruthScreenStatus;
import com.sr.capital.external.truthscreen.repository.TruthScreenDocTypeRepository;
import com.sr.capital.external.truthscreen.service.PanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PanServiceImpl implements PanService {


    private final TruthScreenAdapter truthScreenAdapter;

    private TruthScreenDocTypeRepository truthScreenDocTypeRepository;

    @Override
    public PanResponseDto sendPanRequest(IdSearchRequestDto requestDTO) {
        TruthScreenDocDetails truthScreenDocDetails = truthScreenDocTypeRepository.findBySrCompanyIdAndDocType(RequestData.getTenantId(), TruthScreenDocType.valueOf(requestDTO.getDocType()));
        if(truthScreenDocDetails !=null ){
            //return the details
            return null;
        }
        else if ( truthScreenDocDetails != null && truthScreenDocDetails.getInitialStatus() == TruthScreenStatus.VERIFIED){
            // call AsyncApi
            // check whether the state is complete and status is 1
            // check whether

            return null;
        }
        else if ( truthScreenDocDetails != null && truthScreenDocDetails.getInitialStatus() == TruthScreenStatus.UNVERIFIED ){
            //
            return null;
        }
        return null;

    }

    @Override
    public AsyncReponseDto getAsyncStatus(AsyncRequestDto asyncRequestDto) {
        return null;
    }
}
