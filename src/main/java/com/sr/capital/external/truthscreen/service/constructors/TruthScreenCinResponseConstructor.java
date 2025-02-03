package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.CinDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenCinResponseConstructor implements TruthScreenResponseConstructor {

    @Autowired
    private AES256 aes256;


    @Override
    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request) throws IOException {
        return IdSearchResponseDto.builder()
                .srCompanyId(request.getSrCompanyId())
                .docType(request.getTruthScreenDocType())
                .responses(getDecryptedResponse(request.getDetails()))
                .transId(request.getTransId())
                .build();
    }

    private CinDetails getDecryptedResponse(Object data) throws IOException {
        CinDetails cinDetails = MapperUtils.convertValue(data, CinDetails.class);
        CinDetails.decryptInfo(cinDetails,aes256);
        return cinDetails;

    }
}
