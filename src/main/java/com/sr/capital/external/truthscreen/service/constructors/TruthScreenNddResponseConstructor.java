package com.sr.capital.external.truthscreen.service.constructors;

import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenNddResponseConstructor implements TruthScreenResponseConstructor {


    @Override
    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request) throws IOException {
        return IdSearchResponseDto.builder().docType(request.getTruthScreenDocType())
                .srCompanyId(request.getSrCompanyId())
                .responses(request.getDetails())
                .transId(request.getTransId())
                .build();
    }
}
