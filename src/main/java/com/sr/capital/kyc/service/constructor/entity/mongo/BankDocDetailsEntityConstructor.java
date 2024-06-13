package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BankDocDetailsEntityConstructor implements EntityConstructor {

    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {

        return null;
    }



}
