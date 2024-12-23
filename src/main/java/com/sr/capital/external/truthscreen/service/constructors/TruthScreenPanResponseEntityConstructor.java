package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenPanResponseEntityConstructor implements TruthScreenResponseConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request) throws IOException {
        return IdSearchResponseDto.builder()
                .srCompanyId(request.getSrCompanyId())
                .docType(request.getTruthScreenDocType())
                .responses(getDecryptedPanDetails(request.getDetails())).build();
    }

    private PanDetails getDecryptedPanDetails(Object panDetails) throws IOException {
        PanDetails details = MapperUtils.convertValue(panDetails, PanDetails.class);
        PanDetails.decryptInfo(details,aes256);
        return details;
    }

}
