package com.sr.capital.validation;

import com.sr.capital.dto.request.CompanyKycDetailsRequest;
import com.sr.capital.entity.AdharDetails;
import com.sr.capital.exception.custom.AdharNotFoundException;
import com.sr.capital.repository.CompanyKycRepository;
import com.sr.capital.service.AdharService;
import com.sr.capital.service.RequestValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CompanyKycValidator implements RequestValidator {

    final CompanyKycRepository companyKycRepository;

   // final AdharService adharService;

    @Override
    public <T, U> T validateRequest(U request) throws Exception {
        CompanyKycDetailsRequest companyKycDetailsRequest = (CompanyKycDetailsRequest) request;

        /*AdharDetails adharDetails = adharService.getAdharDetailsById(companyKycDetailsRequest.getAdharId());

        if(adharDetails==null){
            throw new AdharNotFoundException();
        }*/

        //Validate PanId



        return null;
    }
}
