package com.sr.capital.kyc.service.constructor.entity.mongo;


import com.sr.capital.config.AppProperties;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PanAadhaarCrossVerifiedDetailsEntityConstructor implements EntityConstructor {

    @Autowired
    private AppProperties kycAppProperties;

    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {

        return null;
    }



}
