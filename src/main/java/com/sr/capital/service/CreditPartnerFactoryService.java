package com.sr.capital.service;

import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.external.common.GenericCreditPartnerService;
import com.sr.capital.external.incred.service.IncredPartnerService;
import com.sr.capital.external.klub.service.KlubPartnerService;
import com.sr.capital.external.velocity.service.VelocityPartnerService;
import com.sr.capital.service.entityimpl.BaseCreditPartnerEntityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CreditPartnerFactoryService {

    final KlubPartnerService klubPartnerService;
    final VelocityPartnerService velocityPartnerService;
    final IncredPartnerService incredPartnerService;
    final GenericCreditPartnerService genericCreditPartnerService;
    final BaseCreditPartnerEntityServiceImpl baseCreditPartnerEntityService;

    public CreditPartnerService getPartnerService(String creditPartnerName){
        CreditPartnerService creditPartnerService =null;
        switch (creditPartnerName.toLowerCase()){
            case "incred":
                creditPartnerService= incredPartnerService;
                break;
            case "klub":
                creditPartnerService = klubPartnerService;
                break;
            case "velocity":
                 creditPartnerService = velocityPartnerService;
                 break;
            default:
                creditPartnerService =genericCreditPartnerService;
        }
        return creditPartnerService;
    }

}
