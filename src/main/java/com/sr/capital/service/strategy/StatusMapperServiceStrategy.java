package com.sr.capital.service.strategy;

import com.sr.capital.external.common.StatusMapperInterface;
import com.sr.capital.external.flexi.service.FlexiStatusMapperServiceImpl;
import com.sr.capital.service.CreditPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusMapperServiceStrategy {

    @Autowired
    private FlexiStatusMapperServiceImpl flexiPartnerService;


    public StatusMapperInterface getPartnerService(String creditPartnerName) {
        StatusMapperInterface creditPartnerService = null;
        switch (creditPartnerName.toLowerCase()) {
            case "flexi":
                creditPartnerService = flexiPartnerService;
                break;
        }
        return creditPartnerService;
    }
}
