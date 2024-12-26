package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.PanComplianceDetails;
import com.sr.capital.external.truthscreen.entity.PanComprehensiveDetails;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenPanComplianceResponseConstructor;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenPanComprehensiveResponseConstructor;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenPanResponseConstructor;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenResponseConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenIdSearchResponseStrategy {

    @Autowired
    private TruthScreenPanComplianceResponseConstructor truthScreenPanComplianceResponseConstructor;

    @Autowired
    private TruthScreenPanComprehensiveResponseConstructor tRuthScreenPanComprehensiveResponse;

    @Autowired
    private TruthScreenPanResponseConstructor truthScreenPanResponseEntityConstructor;

    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        TruthScreenResponseConstructor entityConstructor = null;
        if (responseClass.equals(PanDetails.class)){
            entityConstructor = truthScreenPanResponseEntityConstructor;
        } else if (responseClass.equals(PanComprehensiveDetails.class)) {
            entityConstructor = tRuthScreenPanComprehensiveResponse;
        } else if (responseClass.equals(PanComplianceDetails.class)) {
            entityConstructor = truthScreenPanComplianceResponseConstructor;
        } else {
            throw new EntityConstructorNotFoundException();
        }
        return entityConstructor.constructResponse(request);
    }
}
