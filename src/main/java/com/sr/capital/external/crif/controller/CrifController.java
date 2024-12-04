package com.sr.capital.external.crif.controller;

import com.sr.capital.external.crif.dto.request.BureauInitiatePayloadRequest;
import com.sr.capital.external.crif.service.CrifPartnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
public class CrifController {

    final CrifPartnerService crifPartnerService;

    public CrifController(CrifPartnerService crifPartnerService) {
        this.crifPartnerService = crifPartnerService;
    }

    //REFACTORED & REVISITED
    @PostMapping(value = "/crif/stage1")
    public ResponseEntity<?> uploadAndExtractDetails(
    ) throws Exception {

        crifPartnerService.initiateBureau(new BureauInitiatePayloadRequest());


        return null;

    }
}