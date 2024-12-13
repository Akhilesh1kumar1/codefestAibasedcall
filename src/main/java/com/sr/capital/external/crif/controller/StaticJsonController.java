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

//    @Value("classpath:static/stage2.json")
//    private Resource stage2;
//
//    @Value("classpath:static/stage3.json")
//    private Resource stage3;

    private final String stage2 = "{\n" +
            "  \"buttonBehaviour\": \"R\",\n" +
            "  \"reportId\": \"CCR190829CR352089576\",\n" +
            "  \"question\": \"Please choose Disbursed Amount range for the latest Loan taken\",\n" +
            "  \"orderId\": \"22111552245\",\n" +
            "  \"optionsList\": [\n" +
            "    \"25lac-50lac \",\n" +
            "    \" 5k-20k \",\n" +
            "    \" 0-5k \",\n" +
            "    \" 50lac-75lac\"\n" +
            "  ],\n" +
            "  \"status\": \"S11\"\n" +
            "}";

    private final String stage3 = "{\n" +
            "  \"B2C-REPORT\": {\n" +
            "    \"HEADER-SEGMENT\": {\n" +
            "      \"DATE-OF-REQUEST\": \"09-12-2024\",\n" +
            "      \"PREPARED-FOR\": \"  \",\n" +
            "      \"PREPARED-FOR-ID\": \"DTC0000345\",\n" +
            "      \"DATE-OF-ISSUE\": \"09-12-2024\",\n" +
            "      \"REPORT-ID\": \"CCR241209CR380672751\",\n" +
            "      \"BATCH-ID\": \"323261099241209\",\n" +
            "      \"STATUS\": \"SUCCESS\",\n" +
            "      \"PRODUCT-TYPE\": \"BBC CONSUMER SCORE\",\n" +
            "      \"PRODUCT-VER\": \"2.0\"\n" +
            "    },\n" +
            "    \"REQUEST-DATA\": {\n" +
            "      \"APPLICANT-SEGMENT\": {\n" +
            "        \"FIRST-NAME\": \"T\",\n" +
            "        \"MIDDLE-NAME\": \"\",\n" +
            "        \"LAST-NAME\": \"KANNAN\",\n" +
            "        \"GENDER\": \"\",\n" +
            "        \"APPLICANT-ID\": \"\",\n" +
            "        \"DOB\": {\n" +
            "          \"DOB-DT\": \"\",\n" +
            "          \"AGE\": \"\",\n" +
            "          \"AGE-AS-ON\": \"\"\n" +
            "        },\n" +
            "        \"IDS\": [],\n" +
            "        \"ADDRESSES\": [\n" +
            "          {\n" +
            "            \"TYPE\": \"D08\",\n" +
            "            \"ADDRESSTEXT\": \"\",\n" +
            "            \"CITY\": \"\",\n" +
            "            \"LOCALITY\": \"\",\n" +
            "            \"STATE\": \"\",\n" +
            "            \"PIN\": \"\",\n" +
            "            \"COUNTRY\": \"\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"PHONES\": [\n" +
            "          {\n" +
            "            \"TYPE\": \"P01\",\n" +
            "            \"VALUE\": \"6673196819\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"EMAILS\": [\n" +
            "          {\n" +
            "            \"EMAIL\": \"abc@abc.com\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"ACCOUNT-NUMBER\": \"\"\n" +
            "      },\n" +
            "      \"APPLICATION-SEGMENT\": {\n" +
            "        \"INQUIRY-UNIQUE-REF-NO\": \"\",\n" +
            "        \"CREDIT-RPT-ID\": null,\n" +
            "        \"CREDIT-RPT-TRN-DT-TM\": \"\",\n" +
            "        \"CREDIT-INQ-PURPS-TYPE\": null,\n" +
            "        \"CREDIT-INQUIRY-STAGE\": null,\n" +
            "        \"CLIENT-CONTRIBUTOR-ID\": \"\",\n" +
            "        \"BRANCH-ID\": null,\n" +
            "        \"APPLICATION-ID\": null,\n" +
            "        \"ACNT-OPEN-DT\": null,\n" +
            "        \"LOAN-AMT\": null,\n" +
            "        \"LTV\": \"\",\n" +
            "        \"TERM\": \"\",\n" +
            "        \"LOAN-TYPE\": \"\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"REQUEST-STATUS\": null,\n" +
            "    \"REPORT-DATA\": {\n" +
            "      \"STANDARD-DATA\": {\n" +
            "        \"DEMOGS\": {\n" +
            "          \"VARIATIONS\": []\n" +
            "        },\n" +
            "        \"EMPLOYMENT-DETAILS\": [],\n" +
            "        \"TRADELINES\": [],\n" +
            "        \"INQUIRY-HISTORY\": [\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"ADANI CAPITAL PRIVATE LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"06-12-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Housing Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"2,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"SCORE\": []\n" +
            "      },\n" +
            "      \"REQUESTED-SERVICES\": null,\n" +
            "      \"ACCOUNTS-SUMMARY\": {\n" +
            "        \"PRIMARY-ACCOUNTS-SUMMARY\": {\n" +
            "          \"NUMBER-OF-ACCOUNTS\": \"0\",\n" +
            "          \"ACTIVE-ACCOUNTS\": \"0\",\n" +
            "          \"OVERDUE-ACCOUNTS\": \"0\",\n" +
            "          \"SECURED-ACCOUNTS\": \"0\",\n" +
            "          \"UNSECURED-ACCOUNTS\": \"0\",\n" +
            "          \"UNTAGGED-ACCOUNTS\": \"0\",\n" +
            "          \"TOTAL-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"CURRENT-BALANCE-SECURED\": \"0.0\",\n" +
            "          \"CURRENT-BALANCE-UNSECURED\": \"0.0\",\n" +
            "          \"TOTAL-SANCTIONED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-DISBURSED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-AMT-OVERDUE\": \"0\"\n" +
            "        },\n" +
            "        \"SECONDARY-ACCOUNTS-SUMMARY\": {\n" +
            "          \"NUMBER-OF-ACCOUNTS\": \"0\",\n" +
            "          \"ACTIVE-ACCOUNTS\": \"0\",\n" +
            "          \"OVERDUE-ACCOUNTS\": \"0\",\n" +
            "          \"SECURED-ACCOUNTS\": \"0.0\",\n" +
            "          \"UNSECURED-ACCOUNTS\": \"0\",\n" +
            "          \"UNTAGGED-ACCOUNTS\": \"0\",\n" +
            "          \"TOTAL-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"TOTAL-SANCTIONED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-DISBURSED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-AMT-OVERDUE\": \"0\"\n" +
            "        },\n" +
            "        \"MFI-GROUP-ACCOUNTS-SUMMARY\": {\n" +
            "          \"NUMBER-OF-ACCOUNTS\": \"0\",\n" +
            "          \"ACTIVE-ACCOUNTS\": \"0\",\n" +
            "          \"OVERDUE-ACCOUNTS\": \"0\",\n" +
            "          \"CLOSED-ACCOUNTS\": \"0\",\n" +
            "          \"NO-OF-OTHER-MFIS\": \"0\",\n" +
            "          \"NO-OF-OWN-MFIS\": \"0\",\n" +
            "          \"TOTAL-OWN-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"TOTAL-OWN-INSTALLMENT-AMT\": \"0\",\n" +
            "          \"TOTAL-OWN-DISBURSED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-OWN-OVERDUE-AMT\": \"0.0\",\n" +
            "          \"TOTAL-OTHER-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"TOTAL-OTHER-INSTALLMENT-AMT\": \"0\",\n" +
            "          \"TOTAL-OTHER-DISBURSED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-OTHER-OVERDUE-AMT\": \"0.0\",\n" +
            "          \"MAX-WORST-DELINQUENCY\": \"0\"\n" +
            "        },\n" +
            "        \"ADDITIONAL-SUMMARY\": [\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS-ACTIVE\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS-DELINQ\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS-ONLY-PRIMARY\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS-ONLY-SECONDARY\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"PERFORM-ATTRIBUTES\": []\n" +
            "      },\n" +
            "      \"TRENDS\": {\n" +
            "        \"NAME\": \"\",\n" +
            "        \"DATES\": \"\",\n" +
            "        \"VALUES\": \"\",\n" +
            "        \"RESERVED1\": \"\",\n" +
            "        \"RESERVED2\": \"\",\n" +
            "        \"RESERVED3\": \"\",\n" +
            "        \"DESCRIPTION\": \"\"\n" +
            "      },\n" +
            "      \"ALERTS\": []\n" +
            "    }\n" +
            "  }\n" +
            "}";
    @PostMapping("/api/stage2")
    public GenericResponse<CrifResponse> getStage1() throws IOException {

//        String content = new String(Files.readAllBytes(stage2.getFile().toPath()));


        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> questionnaireResponse = objectMapper.readValue(stage2, new TypeReference<Map<String, Object>>() {});


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

//        String content = new String(Files.readAllBytes(stage3.getFile().toPath()));

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> reportContent = objectMapper.readValue(stage3, new TypeReference<Map<String, Object>>() {});

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