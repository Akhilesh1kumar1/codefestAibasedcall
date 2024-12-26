package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.PanComplianceDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenPanComplianceResponseConstructor implements TruthScreenResponseConstructor {

    @Autowired
    private AES256 aes256;


    @Override
    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request) throws IOException {
        return IdSearchResponseDto.builder()
                .srCompanyId(request.getSrCompanyId())
                .docType(request.getTruthScreenDocType())
                .responses(getDecryptedPanComplianceDetails(request.getDetails()))
                .build();
    }

    private PanComplianceDetails getDecryptedPanComplianceDetails(Object panComplianceDetails) throws IOException {
        PanComplianceDetails details = MapperUtils.convertValue(panComplianceDetails, PanComplianceDetails.class);
        PanComplianceDetails.decrypt(details, aes256);
        return details;
    }
}
