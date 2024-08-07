package com.sr.capital.external.klub.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.common.GenericCreditPartnerService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KlubPartnerService extends GenericCreditPartnerService {

    final AppProperties appProperties;

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode) throws InvalidVendorCodeException, InvalidVendorTokenException {
        if(!appProperties.getKlubVendorCode().equalsIgnoreCase(vendorCode)){
            throw new InvalidVendorCodeException();
        }

        if(!appProperties.getKlubVendorToken().equalsIgnoreCase(vendorToken)){
            throw new InvalidVendorTokenException();
        }
        return true;
    }
}
