package com.sr.capital.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.client.ShiprocketClient;
import com.sr.capital.service.impl.TestServiceImpl;
import com.sr.capital.util.ResponseBuilderUtil;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController {

    final ShiprocketClient shiprocketClient;
    final TestServiceImpl testService;

    @GetMapping()
    public GenericResponse testValidateTokenApi(@RequestParam("token") String token) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(shiprocketClient.validateToken(token), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    @PostMapping("/jsonpath/{partnerId}")
    public GenericResponse testValidateTokenApi(@PathVariable(name = "partnerId") Long partnerId, @RequestParam Map<String,String> requestParam,@RequestBody Map<String,Object> requestBody) throws UnirestException, CustomException, URISyntaxException, IOException {

        return ResponseBuilderUtil.getResponse(testService.testJsonPath(partnerId,requestParam,requestBody), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }
}
