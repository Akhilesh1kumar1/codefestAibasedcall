package com.sr.capital.kyc.service.strategy;


import com.sr.capital.entity.mongo.kyc.child.*;
import com.sr.capital.exception.EntityConstructorNotFoundException;
import com.sr.capital.kyc.dto.request.DocOrchestratorRequest;
import com.sr.capital.kyc.service.constructor.entity.mongo.*;
import com.sr.capital.kyc.service.interfaces.EntityConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EntityConstructorStrategy {

    @Autowired
    private AadhaarDocDetailsEntityConstructor aadhaarDocDetailsEntityConstructor;

    @Autowired
    private AadhaarVerifiedDetailsEntityConstructor aadhaarVerifiedDetailsEntityConstructor;

    @Autowired
    private BankDocDetailsEntityConstructor bankDocDetailsEntityConstructor;

    @Autowired
    private BankVerifiedDetailsEntityConstructor bankVerifiedDetailsEntityConstructor;

    @Autowired
    private GstDocDetailsEntityConstructor gstDocDetailsEntityConstructor;

    @Autowired
    private GstVerifiedDetailsEntityConstructor gstVerifiedDetailsEntityConstructor;

    @Autowired
    private PanAadhaarCrossVerifiedDetailsEntityConstructor panAadhaarCrossVerifiedDetailsEntityConstructor;

    @Autowired
    private PanDocDetailsEntityConstructor panDocDetailsEntityConstructor;

    @Autowired
    private PanGstCrossVerifiedDetailsEntityConstructor panGstCrossVerifiedDetailsEntityConstructor;

    @Autowired
    private PanVerifiedDetailsEntityConstructor panVerifiedDetailsEntityConstructor;

    @Autowired
    private SelfiDocDetailsConstructor selfiDocDetailsConstructor;


    public <T> T constructEntity(DocOrchestratorRequest request, T entity, Class<?> responseClass) throws EntityConstructorNotFoundException, IOException {
        EntityConstructor entityConstructor;
        if(responseClass.equals(AadhaarDocDetails.class)){
            entityConstructor = aadhaarDocDetailsEntityConstructor;
        } else if(responseClass.equals(AadhaarVerifiedDetails.class)){
            entityConstructor = aadhaarVerifiedDetailsEntityConstructor;
        } else if(responseClass.equals(BankDocDetails.class)){
            entityConstructor = bankDocDetailsEntityConstructor;
        } else if(responseClass.equals(BankVerifiedDetails.class)){
            entityConstructor = bankVerifiedDetailsEntityConstructor;
        } else if(responseClass.equals(GstDocDetails.class)){
            entityConstructor = gstDocDetailsEntityConstructor;
        } else if(responseClass.equals(GstVerifiedDetails.class)){
            entityConstructor = gstVerifiedDetailsEntityConstructor;
        } else if(responseClass.equals(PanAadhaarCrossVerifiedDetails.class)){
            entityConstructor = panAadhaarCrossVerifiedDetailsEntityConstructor;
        } else if(responseClass.equals(PanDocDetails.class)){
            entityConstructor = panDocDetailsEntityConstructor;
        } else if(responseClass.equals(PanGstCrossVerifiedDetails.class)){
            entityConstructor = panGstCrossVerifiedDetailsEntityConstructor;
        } else if(responseClass.equals(PanVerifiedDetails.class)){
            entityConstructor = panVerifiedDetailsEntityConstructor;
        }else if(responseClass.equals(SelfiDocDetails.class)){
            entityConstructor = selfiDocDetailsConstructor;
        }else {
            throw new EntityConstructorNotFoundException();
        }

        return entityConstructor.constructEntity(request, entity);
    }


}
