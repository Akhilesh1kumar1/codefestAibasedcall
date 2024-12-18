package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionResponseData;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.entity.constructors.TruthPanDocDetailsEntityConstructor;
import com.sr.capital.external.truthscreen.service.interfaces.TruthScreenEntityConstructor;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenEntityConstructorStrategy {

    @Autowired
    private TruthPanDocDetailsEntityConstructor truthPanDocDetailsEntityConstructor;

    public <T> T constructEntity(TruthScreenDocDetails request, T entity, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        TruthScreenEntityConstructor entityConstructor;
        if (responseClass.equals(PanDetails.class)){
//            entityConstructor = truthPanDocDetailsEntityConstructor;
        }
        else {
            throw new EntityConstructorNotFoundException();
        }
        return (T) entityConstructor.constructEntity(request,entity);
    }
}
