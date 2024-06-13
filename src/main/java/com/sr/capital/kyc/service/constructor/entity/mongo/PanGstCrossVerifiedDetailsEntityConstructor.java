package com.sr.capital.kyc.service.constructor.entity.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PanGstCrossVerifiedDetailsEntityConstructor implements EntityConstructor {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private AppProperties kycAppProperties;


    @Override
    @SuppressWarnings("unchecked")
    public <T> T constructEntity(DocOrchestratorRequest request, T entity) {

      return null;
    }



}
