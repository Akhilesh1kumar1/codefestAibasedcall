package com.sr.capital.external.truthscreen.entity.constructors;

import com.sr.capital.dto.RequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionResponseData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenPanExtractionResponse;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class TruthPanDocDetailsEntityConstructor{ /*implements TruthScreenEntityConstructor {

    @Override
    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity) {
        TruthScreenPanExtractionResponse response = (TruthScreenPanExtractionResponse) request.getTruthScreenBaseResponse();
        PanExtractionResponseData result = null;
        PanDetails panDetails = null;

        return (T) TruthScreenDocDetails.builder()
                .srCompanyId(RequestData.getTenantId())
                .truthScreenDocType(TruthScreenDocType.PAN)
                .details(PanExtractionRequestData.class)
                .build();

    }

*/
}
