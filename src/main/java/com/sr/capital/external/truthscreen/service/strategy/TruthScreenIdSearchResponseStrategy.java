package com.sr.capital.external.truthscreen.service.strategy;

import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.external.truthscreen.dto.response.IdSearchResponseDto;
import com.sr.capital.external.truthscreen.entity.PanDetails;
import com.sr.capital.external.truthscreen.entity.TruthScreenDocDetails;
import com.sr.capital.external.truthscreen.service.constructors.TruthScreenPanResponseEntityConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TruthScreenIdSearchResponseStrategy {

    @Autowired
    private TruthScreenPanResponseEntityConstructor truthScreenPanResponseEntityConstructor;

    public IdSearchResponseDto<?> constructResponse(TruthScreenDocDetails request, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        TruthScreenPanResponseEntityConstructor entityConstructor = null;
        if (responseClass.equals(PanDetails.class)){
            entityConstructor = truthScreenPanResponseEntityConstructor;
        }
        else {
            throw new EntityConstructorNotFoundException();
        }
        return entityConstructor.constructResponse(request);
    }
}
