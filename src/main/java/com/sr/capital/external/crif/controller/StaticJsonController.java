package com.sr.capital.external.crif.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.util.ResponseBuilderUtil;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

import static com.omunify.core.util.Constants.StatusEnum.SUCCESS;
import static com.sr.capital.helpers.constants.Constants.MessageConstants.REQUEST_SUCCESS;

@RestController
public class StaticJsonController {

    @Value("classpath:static/stage2.json")
    private Resource stage2;

    @Value("classpath:static/stage3.json")
    private Resource stage3;

    @PostMapping("/api/stage2")
    public GenericResponse<CrifResponse> getStage1() throws IOException {

        String content = new String(Files.readAllBytes(stage2.getFile().toPath()));


        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> questionnaireResponse = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});


        return ResponseBuilderUtil.getResponse(
                CrifResponse.builder()
                        .status("QUESTIONS_GENERATED")
                        .questionnaireResponse(questionnaireResponse)
                        .build(),
                SUCCESS,
                REQUEST_SUCCESS,
                HttpStatus.SC_OK
        );
    }

    @PostMapping("/api/stage3")
    public GenericResponse<CrifResponse> getStage2() throws IOException {

        String content = new String(Files.readAllBytes(stage3.getFile().toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> reportContent = objectMapper.readValue(content, new TypeReference<Map<String, Object>>() {});

        return ResponseBuilderUtil.getResponse(
                CrifResponse.builder()
                        .status("REPORT_GENERATED")
                        .report(reportContent)
                        .build(),
                SUCCESS,
                REQUEST_SUCCESS,
                HttpStatus.SC_OK
        );
    }
}