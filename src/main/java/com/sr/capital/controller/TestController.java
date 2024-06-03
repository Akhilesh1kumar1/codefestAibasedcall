package com.sr.capital.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.client.ShiprocketClient;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    final ShiprocketClient shiprocketClient;


    @GetMapping()
    public GenericResponse testValidateTokenApi(@RequestParam("token") String token) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(shiprocketClient.validateToken(token), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }
}
