package com.sr.capital.external.velocity.service;

import com.sr.capital.external.common.GenericCreditPartnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VelocityPartnerService extends GenericCreditPartnerService {

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode) {
        return null;
    }
}
