package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenDocOrchestratorRequest;
import com.sr.capital.external.truthscreen.entity.PanComplianceDetails;
import com.sr.capital.external.truthscreen.entity.PanComprehensiveDetails;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.constructors.TruthPanDocDetailsEntityConstructor;
import com.sr.capital.external.truthscreen.entity.constructors.TruthScreenPanComprehensiveEntityConstructor;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenPanComplianceEntityConstructor;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenEntityConstructorStrategy {

    @Autowired
    private TruthScreenPanComplianceEntityConstructor truthScreenPanComplianceEntityConstructor;

    @Autowired
    private TruthScreenPanComprehensiveEntityConstructor truthScreenPanComprehensiveEntityConstructor;

    @Autowired
    private TruthPanDocDetailsEntityConstructor truthPanDocDetailsEntityConstructor;

    public <T> T constructEntity(TruthScreenDocOrchestratorRequest request, T entity, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        TruthScreenEntityConstructor entityConstructor = null;
        if (responseClass.equals(PanDetails.class)){
               entityConstructor = truthPanDocDetailsEntityConstructor;
        }
        else if (responseClass.equals(PanComprehensiveDetails.class)){
            entityConstructor = truthScreenPanComprehensiveEntityConstructor;
        } else if (responseClass.equals(PanComplianceDetails.class)) {
            entityConstructor = truthScreenPanComplianceEntityConstructor;
        } else {
            throw new EntityConstructorNotFoundException();
        }
        return entityConstructor.constructEntity(request,entity);
    }
}
