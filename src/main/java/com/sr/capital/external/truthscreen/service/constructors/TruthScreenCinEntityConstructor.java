package com.sr.capital.external.truthscreen.service.constructors;

import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.CinExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.CinDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.util.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class TruthScreenCinEntityConstructor implements TruthScreenEntityConstructor {

    @Autowired
    private AES256 aes256;

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) throws IOException {
        CinExtractionResponseData cinExtractionResponseData = MapperUtils.convertValue(request.getTruthScreenBaseResponse(), CinExtractionResponseData.class);
        CinDetails gstinDetails = getCINdetails(cinExtractionResponseData);
        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .transId(request.getTransId())
                .initialStatus(request.getTruthScreenBaseResponse().getStatus())
                .details(gstinDetails)
                .truthScreenDocType(request.getDocType())
                .build();

    }

    public CinDetails getCINdetails(CinExtractionResponseData extractionRequestData){
        CinDetails cinDetails = CinDetails.builder().msg(extractionRequestData.getMsg()).status(extractionRequestData.getStatus()).build();
        CinDetails.encryptInfo(cinDetails,aes256);
        return cinDetails;
    }
}
