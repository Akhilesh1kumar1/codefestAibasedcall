package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.*;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenGstinResponseConstructor implements TruthScreenResponseConstructor {

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

    public GSTinDetails getDecryptedResponse(Object gstinDetails) throws IOException {
        GSTinDetails gsTinDetails = MapperUtils.convertValue(gstinDetails, GSTinDetails.class);
        GSTinDetails.decryptInfo(gsTinDetails,aes256);
        return gsTinDetails;

    }
}
