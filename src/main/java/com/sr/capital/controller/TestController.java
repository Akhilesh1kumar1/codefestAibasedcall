package com.sr.capital.controller;

import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.omunify.core.util.Constants;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.external.service.CommunicationService;
import com.sr.capital.repository.secondary.TestRepository;
import com.sr.capital.service.impl.ServicesHandler;
import com.sr.capital.service.impl.TestServiceImpl;
import com.sr.capital.external.shiprocket.client.ShiprocketClient;
import com.sr.capital.external.shiprocket.dto.response.KycResponse;
import com.sr.capital.util.ResponseBuilderUtil;
import com.sr.capital.util.WebClientUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.apache.http.auth.InvalidCredentialsException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import static com.sr.capital.helpers.enums.ServiceName.SHIPROCKET;

@RestController
@RequestMapping("/api/v1/test")
@RequiredArgsConstructor
public class TestController {

    final ShiprocketClient shiprocketClient;
    final TestServiceImpl testService;
    final TestRepository testRepository;
    final ServicesHandler servicesHandler;
    final CommunicationService communicationService;
    final WebClientUtil webClientUtil;

    @GetMapping()
    public GenericResponse testValidateTokenApi(@RequestParam("token") String token) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(shiprocketClient.validateToken(token), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    @PostMapping("/jsonpath/{partnerId}")
    public GenericResponse testValidateTokenApi(@PathVariable(name = "partnerId") Long partnerId, @RequestParam Map<String,String> requestParam,@RequestBody Map<String,Object> requestBody) throws UnirestException, CustomException, URISyntaxException, IOException {

        return ResponseBuilderUtil.getResponse(testService.testJsonPath(partnerId, requestParam, requestBody), Constants.StatusEnum.SUCCESS, "", HttpStatus.SC_OK);
    }
    @GetMapping("/kyc")
    public GenericResponse<KycResponse> kycDetails(@RequestParam("token") String token) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(shiprocketClient.getKycDetails(token), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    @GetMapping("/db")
    public GenericResponse testMultiteDb(@RequestParam("token") String token) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(testRepository.findAll(), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    @GetMapping("/template/{partner}/access-token")
    public GenericResponse<Object> testPartnerTemplate(
            HttpServletRequest request,
            @PathVariable(name = "partner") String partner
    ) throws InvalidCredentialsException, CustomException {
        servicesHandler.validateSelfSecret(request);
        return ResponseBuilderUtil.getResponse(
                testService.testAccessToken(partner),
                Constants.StatusEnum.SUCCESS, "", HttpStatus.SC_OK);
    }


    @GetMapping("/loan/details")
    public GenericResponse testMultiteDb(@RequestParam("loanId") String loanId,@RequestParam("partnerName") String loanPartnerName) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(testService.testAccessToken(loanPartnerName,loanId), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    @GetMapping("/email")
    public GenericResponse testEmailId(@RequestParam("emailId") String emailid,@RequestParam("userName") String userName) throws UnirestException, CustomException {

        return ResponseBuilderUtil.getResponse(communicationService.testCommunicationRequestForEmailVerification(emailid,userName,"https://codebeautify.org/htmlviewer"), Constants.StatusEnum.SUCCESS,"",  HttpStatus.SC_OK);
    }

    //TODO ::
    @PostMapping("/redis-set-temp-value")
    public GenericResponse test()  {

        return ResponseBuilderUtil.getResponse(testService.setTempValueInRadis(),Constants.StatusEnum.SUCCESS,
                "done",  HttpStatus.SC_OK);
    }
    @GetMapping("/redis-set-temp-value1")
    public GenericResponse test1()  {

        return ResponseBuilderUtil.getResponse(testService.getTempValueInRadis(),Constants.StatusEnum.SUCCESS,
                "done",  HttpStatus.SC_OK);
    }
}
