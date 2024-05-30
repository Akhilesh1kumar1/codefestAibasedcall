package com.sr.capital.controller;


import com.omunify.core.model.GenericResponse;
import com.sr.capital.helpers.enums.RequestType;
import com.sr.capital.service.impl.RSAUtilityService;
import com.sr.capital.service.impl.ServicesHandler;
import com.sr.capital.util.ResponseBuilderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.VALID_PAN;

@RestController
@RequestMapping("/util")
@AllArgsConstructor
public class UtilityController {

    final ServicesHandler servicesHandler;

    final RSAUtilityService rsaUtilityService;



    @PostMapping("/generate_rsa_keys")
    public GenericResponse<Boolean> generateRsaKeys(HttpServletRequest request) throws Exception {
        servicesHandler.validateSelfSecret(request);
        rsaUtilityService.generateRsaKeyPairs();
        return ResponseBuilderUtil.getResponse( true,SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);
    }

    @GetMapping("/public_key")
    public GenericResponse<String> getPublicKey() throws Exception {

        return ResponseBuilderUtil.getResponse( rsaUtilityService.getPublicKeyString(),SUCCESS,
                VALID_PAN, HttpStatus.SC_OK);
    }

}
