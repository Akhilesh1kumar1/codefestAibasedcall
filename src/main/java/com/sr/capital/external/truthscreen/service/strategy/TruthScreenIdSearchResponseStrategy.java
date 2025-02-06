package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.*;
import com.sr.capital.external.truthscreen.service.constructors.*;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class TruthScreenIdSearchResponseStrategy {

    private final TruthScreenNddResponseConstructor truthScreenNddResponseConstructor;

    private final TruthScreenCinResponseConstructor truthScreenCinResponseConstructor;

    private final TruthScreenGstinResponseConstructor truthScreenGstinResponseConstructor;

    private final TruthScreenPanToGstResponseConstructor truthScreenPanToGstResponseConstructor;

    private final TruthScreenPanComplianceResponseConstructor truthScreenPanComplianceResponseConstructor;
    private final TruthScreenPanComprehensiveResponseConstructor tRuthScreenPanComprehensiveResponse;

    private final TruthScreenPanResponseConstructor truthScreenPanResponseEntityConstructor;

    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        TruthScreenResponseConstructor entityConstructor = null;
        if (responseClass.equals(PanDetails.class)){
            entityConstructor = truthScreenPanResponseEntityConstructor;
        } else if (responseClass.equals(PanComprehensiveDetails.class)) {
            entityConstructor = tRuthScreenPanComprehensiveResponse;
        } else if (responseClass.equals(PanComplianceDetails.class)) {
            entityConstructor = truthScreenPanComplianceResponseConstructor;
        } else if (responseClass.equals(PanToGstDetails.class)) {
            entityConstructor = truthScreenPanToGstResponseConstructor;
        } else if (responseClass.equals(GSTinDetails.class)) {
            entityConstructor = truthScreenGstinResponseConstructor;
        } else if (responseClass.equals(CinDetails.class)) {
            entityConstructor = truthScreenCinResponseConstructor;
        } else if (responseClass.equals(NddDetails.class)) {
            entityConstructor = truthScreenNddResponseConstructor;
        } else {
            throw new EntityConstructorNotFoundException();
        }
        return entityConstructor.constructResponse(request);
    }
}
