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

    private final String stage3_1 = "{\n" +
            "  \"B2C-REPORT\": {\n" +
            "    \"HEADER-SEGMENT\": {\n" +
            "      \"DATE-OF-REQUEST\": \"13-07-2023\",\n" +
            "      \"PREPARED-FOR\": \"  \",\n" +
            "      \"PREPARED-FOR-ID\": \"MFI00\",\n" +
            "      \"DATE-OF-ISSUE\": \"13-07-2023\",\n" +
            "      \"REPORT-ID\": \"CCR230713CR376039761\",\n" +
            "      \"BATCH-ID\": \"318620019230713\",\n" +
            "      \"STATUS\": \"SUCCESS\",\n" +
            "      \"PRODUCT-TYPE\": \"BBC CONSUMER SCORE\",\n" +
            "      \"PRODUCT-VER\": \"2.0\"\n" +
            "    },\n" +
            "    \"REQUEST-DATA\": {\n" +
            "      \"APPLICANT-SEGMENT\": {\n" +
            "        \"FIRST-NAME\": \"LATHA\",\n" +
            "        \"MIDDLE-NAME\": \"\",\n" +
            "        \"LAST-NAME\": \"GANESHMURTHY\",\n" +
            "        \"GENDER\": \"\",\n" +
            "        \"APPLICANT-ID\": \"\",\n" +
            "        \"DOB\": {\n" +
            "          \"DOB-DT\": \"12-09-1996\",\n" +
            "          \"AGE\": \"\",\n" +
            "          \"AGE-AS-ON\": \"\"\n" +
            "        },\n" +
            "        \"IDS\": [\n" +
            "          {\n" +
            "            \"TYPE\": \"ID07\",\n" +
            "            \"VALUE\": \"XAPPX9621O\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"ADDRESSES\": [\n" +
            "          {\n" +
            "            \"TYPE\": \"D08\",\n" +
            "            \"ADDRESSTEXT\": \"W O GANESHAMURTHY 33 KANTHAM PALAYAM SEMBIYA NALLUR AVINASHI AVI TN COIMBATORE COIMBATORE \",\n" +
            "            \"CITY\": \"COIMBATORE\",\n" +
            "            \"LOCALITY\": \"COIMBATORE\",\n" +
            "            \"STATE\": \"TN\",\n" +
            "            \"PIN\": \"641654\",\n" +
            "            \"COUNTRY\": \"india\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"PHONES\": [\n" +
            "          {\n" +
            "            \"TYPE\": \"P01\",\n" +
            "            \"VALUE\": \"6688493648\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"EMAILS\": [\n" +
            "          {\n" +
            "            \"EMAIL\": \"anil.shejale@crifhighmark.com\"\n" +
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
            "          \"VARIATIONS\": [\n" +
            "            {\n" +
            "              \"TYPE\": \"PAN-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"XAPPX9621O\",\n" +
            "                  \"REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"AKFPR5974D\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"10-02-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Business Loan Priority Sector  Small Business\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"ATPPA8046G\",\n" +
            "                  \"REPORTED-DT\": \"31-01-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"22-04-2016\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Prime Minister Jaan Dhan Yojana - Overdraft\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"BXEPM7228J\",\n" +
            "                  \"REPORTED-DT\": \"31-07-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"10-04-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Prime Minister Jaan Dhan Yojana - Overdraft\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"TYPE\": \"ADDRESS-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"W/O.GANESHAMURTHY, 33, KANTHAM PALAYAM SEMBIYA NALLUR, AVINASHI AVI 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-07-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"22-04-2016\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Prime Minister Jaan Dhan Yojana - Overdraft\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"33  KANDHAM PALAYAM  VELLIYAM PALAYAM PO  AVINASHI 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"03-06-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"21-11-2013\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"DOOR NO 3/67-1 KANTHAMPALAYAM SEMBIYANALLUR AVINASHI COIMBATORE  -ANNUR 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-08-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"30-03-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"DOOR NO 3/67-1 KANTHAMPALAYAM SEMBIYANALLUR AVINASHI COIMBATORE ANNUR 191 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-05-2019\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"20-06-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Personal Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"3/67-1 KANTHAMPALAYAM W.NO 1  SEMBINALLUR AVINASHI 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"09-09-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"3/67KANTHAMPALAYAMSEMBIANALLURTIRUP UR  B_SEMBIANALLUR_KANDAMPALAYAM AVINASHI 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"29-02-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"19-07-2019\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"GANESAMURHTY 3/67-1 KANTHAMPALAYAM W NO 1SEMBIYANALLUR  TIRUPUR 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-10-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"20-02-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"33  KANDHAM PALAYAM  VELLIYAM PALAYAM PO  AVINASHI 641654 KL \",\n" +
            "                  \"REPORTED-DT\": \"02-11-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"05-09-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"W/O.GANESHAMURTHY, 33, KANTHAM PALAYAM SEMBIYA NALLUR, AVINASHI AVI TN 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"30-09-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"30-09-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Business Loan Priority Sector  Agriculture\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"NO 3/67-1 D NO 33 KANTHAMPALAYAM W NO 1 AVINASHI AVINASHI AVINASHI 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-12-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"14-07-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"3 67 1 KANTHAM PALAYAM SEMIANALLUR TIRUPPUR 641654 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"04-07-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"17-08-2016\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"DOOR NO 3/67-1 KANTHAMPALAYAM SEMBIANALLUR AVINASHI TIRUPPUR 512 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-05-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"21-04-2019\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": null,\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"3/67-1 KANTHAMPALAYAM W.NO 1 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"31-12-2014\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"31-12-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Business Loan Priority Sector  Small Business\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"SREE SREE FASHION KANTHAMPALAYAM AVINASH I COIMBATORE 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"11-01-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Consumer Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"NO-3/67-1 VINAYAGAR KOVIL 1ST STREET KAN THAMPALAYAM AVINASHI - NEAR VINAYAGAR KO VIL COIMBATORE 641654 TN \",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"11-01-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Consumer Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"3 67 KANTHAMPALAYAM SEMBIYANALLUR TIRUPPUR 641654 641654 BR \",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"15-07-2019\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"TYPE\": \"PHONE-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"6515525846\",\n" +
            "                  \"REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"09-09-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"TYPE\": \"NAME-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"LATHA G\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"10-02-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"null,null,Prime Minister Jaan Dhan Yojana - Overdraft,MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF,NBF,NAB,MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"LATHA\",\n" +
            "                  \"REPORTED-DT\": \"02-11-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"08-09-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"LATHA GANESAMOORTHY\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"02-08-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"TYPE\": \"DOB-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"12-03-1976\",\n" +
            "                  \"REPORTED-DT\": \"31-08-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"30-03-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"28-10-1979\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"02-08-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"01-09-1979\",\n" +
            "                  \"REPORTED-DT\": \"31-10-2019\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"21-04-2019\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": null,\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"31-12-1979\",\n" +
            "                  \"REPORTED-DT\": \"31-10-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"21-02-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"01-01-1979\",\n" +
            "                  \"REPORTED-DT\": \"31-01-2019\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"20-06-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Personal Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"13-05-1979\",\n" +
            "                  \"REPORTED-DT\": \"31-03-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"20-11-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"18-03-1976\",\n" +
            "                  \"REPORTED-DT\": \"02-11-2016\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"08-09-2014\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"MFI\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"31-12-1976\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"10-02-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Business Loan Priority Sector  Small Business\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"01-08-1979\",\n" +
            "                  \"REPORTED-DT\": \"30-06-2020\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"11-01-2017\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Consumer Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NBF\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"12-09-1976\",\n" +
            "                  \"REPORTED-DT\": \"31-01-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"22-04-2016\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Prime Minister Jaan Dhan Yojana - Overdraft\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            {\n" +
            "              \"TYPE\": \"VOTERID-VARIATIONS\",\n" +
            "              \"VARIATION\": [\n" +
            "                {\n" +
            "                  \"VALUE\": \"18837078138984\",\n" +
            "                  \"REPORTED-DT\": \"30-09-2018\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"30-09-2018\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"Business Loan Priority Sector  Agriculture\",\n" +
            "                  \"SOURCE-INDICATOR\": \"NAB\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"VALUE\": \"41557410200845\",\n" +
            "                  \"REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"FIRST-REPORTED-DT\": \"01-07-2015\",\n" +
            "                  \"LOAN-TYPE-ASSOC\": \"MFI Loan\",\n" +
            "                  \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          ]\n" +
            "        },\n" +
            "        \"EMPLOYMENT-DETAILS\": [\n" +
            "          {\n" +
            "            \"EMPLOYMENT-DETAIL\": {\n" +
            "              \"OCCUPATION\": \"OTHERS\",\n" +
            "              \"FIRST-REPORTED-DT\": \"31-12-2014\",\n" +
            "              \"LAST-REPORTED-DT\": \"31-03-2015\",\n" +
            "              \"ACCT-TYPE\": \"Business Loan Priority Sector  Small Business\",\n" +
            "              \"SOURCE-INDICATOR\": \"PRB\"\n" +
            "            }\n" +
            "          }\n" +
            "        ],\n" +
            "        \"TRADELINES\": [\n" +
            "          {\n" +
            "            \"mfiId\": \"NBF0000082\",\n" +
            "            \"ACCT-NUMBER\": \"REKDK913284948\",\n" +
            "            \"CREDIT-GRANTOR\": \"FICCL\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NBF\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"28-12-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"37,706\",\n" +
            "            \"DISBURSED-DT\": \"30-06-2022\",\n" +
            "            \"INSTALLMENT-AMT\": \"1,995/Monthly\",\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"14,610\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F03\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"24\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2022-11-28 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"1,000\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": \"\",\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2022|Nov:2022|Oct:2022|Sep:2022|Aug:2022|Jul:2022|\",\n" +
            "                \"VALUES\": \"XXX/XXX|000/XXX|000/XXX|030/XXX|030/XXX|001/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2022|Nov:2022|Oct:2022|Sep:2022|Aug:2022|Jul:2022|\",\n" +
            "                \"VALUES\": \"14,610|16,279|17,914|19,518|21,090|22,630|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NAB0000018\",\n" +
            "            \"ACCT-NUMBER\": \"PPUGS184466961\",\n" +
            "            \"CREDIT-GRANTOR\": \"UNION BANK OF INDIA\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category C\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NAB\",\n" +
            "            \"ACCT-TYPE\": \"BUSINESS LOAN PRIORITY SECTOR  AGRICULTURE\",\n" +
            "            \"REPORTED-DT\": \"30-09-2018\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"05-09-2018\",\n" +
            "            \"DISBURSED-AMT\": \"90,000\",\n" +
            "            \"DISBURSED-DT\": \"03-09-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": \"75,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"1,89,760\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"Monthly\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"INTEREST-RATE\": \"10.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|\",\n" +
            "                \"VALUES\": \"000/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"90,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"0||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": \"0\",\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NAB0000018\",\n" +
            "            \"ACCT-NUMBER\": \"QTWUE646812861\",\n" +
            "            \"CREDIT-GRANTOR\": \"UNION BANK OF INDIA\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category C\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NAB\",\n" +
            "            \"ACCT-TYPE\": null,\n" +
            "            \"REPORTED-DT\": \"31-03-2019\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"14-03-2019\",\n" +
            "            \"DISBURSED-AMT\": \"10,000\",\n" +
            "            \"DISBURSED-DT\": \"16-10-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": \"75,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"Monthly\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"INTEREST-RATE\": \"10.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|\",\n" +
            "                \"VALUES\": \"000/STD|XXX/XXX|000/STD|000/STD|000/STD|000/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|\",\n" +
            "                \"VALUES\": \"10,000||10,000|10,000|10,000|10,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|\",\n" +
            "                \"VALUES\": \"0||10,000|10,000|10,000|10,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|\",\n" +
            "                \"VALUES\": \"|||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": null,\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": \"0\",\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"PRB0000003\",\n" +
            "            \"ACCT-NUMBER\": \"KPOYI381692768\",\n" +
            "            \"CREDIT-GRANTOR\": \"HDFC BANK LTD\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category A\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"PRB\",\n" +
            "            \"ACCT-TYPE\": \"BUSINESS LOAN PRIORITY SECTOR  SMALL BUSINESS\",\n" +
            "            \"REPORTED-DT\": \"30-06-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"20-06-2015\",\n" +
            "            \"DISBURSED-AMT\": \"1,25,690\",\n" +
            "            \"DISBURSED-DT\": \"25-08-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": null,\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"ACTUAL-PAYMENT\": \"35,111\",\n" +
            "            \"LAST-PAYMENT-DT\": \"2021-06-20 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|Oct:2014|Sep:2014|Aug:2014|Jul:2014|Jun:2014|May:2014|Apr:2014|Mar:2014|Feb:2014|Jan:2014|Dec:2013|Nov:2013|Oct:2013|Sep:2013|Aug:2013|Jul:2013|Jun:2013|May:2013|Apr:2013|Mar:2013|Feb:2013|Jan:2013|Dec:2012|Nov:2012|Oct:2012|Sep:2012|Aug:2012|Jul:2012|Jun:2012|May:2012|Apr:2012|\",\n" +
            "                \"VALUES\": \"000/STD|000/STD|000/STD|000/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|\",\n" +
            "                \"VALUES\": \"1,25,690|1,25,690|1,25,690|1,25,690||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|\",\n" +
            "                \"VALUES\": \"56,319|66,877|77,212|87,327||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|\",\n" +
            "                \"VALUES\": \"12,006|12,006|12,006|12,006||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": \"0\",\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": \"35,111\",\n" +
            "            \"OCCUPATION\": \"Others\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NBF0000324\",\n" +
            "            \"ACCT-NUMBER\": \"WYLIH189888139\",\n" +
            "            \"CREDIT-GRANTOR\": \"BAJAJ FINANCE LIMITED\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NBF\",\n" +
            "            \"ACCT-TYPE\": \"CONSUMER LOAN\",\n" +
            "            \"REPORTED-DT\": \"30-06-2020\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"21-12-2017\",\n" +
            "            \"DISBURSED-AMT\": \"42,000\",\n" +
            "            \"DISBURSED-DT\": \"30-11-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": null,\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2021-11-22 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|Aug:2015|Jul:2015|Jun:2015|May:2015|Apr:2015|Mar:2015|Feb:2015|Jan:2015|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|\",\n" +
            "                \"VALUES\": \"42,000|42,000|42,000|42,000|42,000|42,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|\",\n" +
            "                \"VALUES\": \"0|0|0|3,582|7,141|10,677||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|\",\n" +
            "                \"VALUES\": \"|||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NAB0000018\",\n" +
            "            \"ACCT-NUMBER\": \"MGXIL462151030\",\n" +
            "            \"CREDIT-GRANTOR\": \"UNION BANK OF INDIA\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category C\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NAB\",\n" +
            "            \"ACCT-TYPE\": null,\n" +
            "            \"REPORTED-DT\": \"10-09-2019\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"10-09-2019\",\n" +
            "            \"DISBURSED-AMT\": \"20,000\",\n" +
            "            \"DISBURSED-DT\": \"14-03-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": \"75,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"On Demand \",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"INTEREST-RATE\": \"10.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|\",\n" +
            "                \"VALUES\": \"000/STD|XXX/XXX|000/STD|000/STD|XXX/XXX|000/STD|000/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|\",\n" +
            "                \"VALUES\": \"20,000||20,000|20,000||20,000|20,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|\",\n" +
            "                \"VALUES\": \"0||20,070|20,070||20,070|20,070||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|\",\n" +
            "                \"VALUES\": \"||||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": null,\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NAB0000018\",\n" +
            "            \"ACCT-NUMBER\": \"PPUGS184466961\",\n" +
            "            \"CREDIT-GRANTOR\": \"UNION BANK OF INDIA\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category C\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NAB\",\n" +
            "            \"ACCT-TYPE\": \"BUSINESS LOAN PRIORITY SECTOR  AGRICULTURE\",\n" +
            "            \"REPORTED-DT\": \"31-03-2019\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"12-03-2019\",\n" +
            "            \"DISBURSED-AMT\": \"80,000\",\n" +
            "            \"DISBURSED-DT\": \"05-09-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": \"75,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"Monthly\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"INTEREST-RATE\": \"10.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|\",\n" +
            "                \"VALUES\": \"000/STD|XXX/XXX|000/STD|000/STD|000/STD|000/STD|000/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"80,000||80,000|80,000|80,000|80,000|80,000||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"0||80,399|80,399|80,399|80,399|80,399||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|\",\n" +
            "                \"VALUES\": \"||||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": \"0\",\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NBF0000082\",\n" +
            "            \"ACCT-NUMBER\": \"RSZUJ335361016\",\n" +
            "            \"CREDIT-GRANTOR\": \"FICCL\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NBF\",\n" +
            "            \"ACCT-TYPE\": \"PERSONAL LOAN\",\n" +
            "            \"REPORTED-DT\": \"31-05-2019\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Closed\",\n" +
            "            \"CLOSED-DT\": \"09-03-2019\",\n" +
            "            \"DISBURSED-AMT\": \"50,000\",\n" +
            "            \"DISBURSED-DT\": \"13-04-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": null,\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2021-03-08 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": \"0\",\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|\",\n" +
            "                \"VALUES\": \"50,000|50,000|50,000|50,000|50,000|50,000|50,000|50,000|50,000|50,000|50,000|50,000|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|\",\n" +
            "                \"VALUES\": \"0|5,000|7,710|10,330|12,890|15,360|17,780|20,110|22,380|24,600|26,730|28,830|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|\",\n" +
            "                \"VALUES\": \"||||||||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" ;
            String stage3_2 =
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NAB0000018\",\n" +
            "            \"ACCT-NUMBER\": \"FZPZU746925998\",\n" +
            "            \"CREDIT-GRANTOR\": \"UNION BANK OF INDIA\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category C\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NAB\",\n" +
            "            \"ACCT-TYPE\": \"PRIME MINISTER JAAN DHAN YOJANA - OVERDRAFT\",\n" +
            "            \"REPORTED-DT\": \"31-07-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"5,000\",\n" +
            "            \"DISBURSED-DT\": \"01-09-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"-171\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"Monthly\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"INTEREST-RATE\": \"11.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2022|Jun:2022|May:2022|Apr:2022|Mar:2022|Feb:2022|Jan:2022|Dec:2021|Nov:2021|Oct:2021|Sep:2021|Aug:2021|Jul:2021|Jun:2021|May:2021|Apr:2021|Mar:2021|Feb:2021|Jan:2021|Dec:2020|Nov:2020|Oct:2020|Sep:2020|Aug:2020|Jul:2020|Jun:2020|May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|\",\n" +
            "                \"VALUES\": \"000/STD|090/STD|180/STD|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|000/STD|XXX/XXX|000/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|XXX/STD|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2022|Jun:2022|May:2022|Apr:2022|Mar:2022|Feb:2022|Jan:2022|Dec:2021|Nov:2021|Oct:2021|Sep:2021|Aug:2021|\",\n" +
            "                \"VALUES\": \"5,000|5,000|5,000|5,000||5,000|5,000|5,000|5,000|5,000|5,000|5,000|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2022|Jun:2022|May:2022|Apr:2022|Mar:2022|Feb:2022|Jan:2022|Dec:2021|Nov:2021|Oct:2021|Sep:2021|Aug:2021|\",\n" +
            "                \"VALUES\": \"-171|-170|-24|-26||3,872|4,116|4,649|4,836|5,011|4,809|4,736|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2022|Jun:2022|May:2022|Apr:2022|Mar:2022|Feb:2022|Jan:2022|Dec:2021|Nov:2021|Oct:2021|Sep:2021|Aug:2021|\",\n" +
            "                \"VALUES\": \"||||||||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Secured\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": \"0\",\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"NBF0000082\",\n" +
            "            \"ACCT-NUMBER\": \"OEWLC857250252\",\n" +
            "            \"CREDIT-GRANTOR\": \"FICCL\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"NBF\",\n" +
            "            \"ACCT-TYPE\": null,\n" +
            "            \"REPORTED-DT\": \"31-05-2020\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"59,188\",\n" +
            "            \"DISBURSED-DT\": \"30-04-2021\",\n" +
            "            \"INSTALLMENT-AMT\": null,\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"59,188\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": null,\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": null,\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": null,\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": \"0\",\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": \"\",\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|XXX/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|Jun:2019|\",\n" +
            "                \"VALUES\": \"59,188|59,188|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|1,00,000|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|Jun:2019|\",\n" +
            "                \"VALUES\": \"59,188|59,188|56,706|56,706|60,844|64,788|68,637|72,450|76,115|79,754|83,244|86,650|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|Jun:2019|\",\n" +
            "                \"VALUES\": \"0|0|||||||||||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": null,\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": \"Restructured due to COVID-19\",\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000105\",\n" +
            "            \"ACCT-NUMBER\": \"KMHPI007894532\",\n" +
            "            \"CREDIT-GRANTOR\": \"PANCHRATNA SECURITIES\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"30-06-2020\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"40,000\",\n" +
            "            \"DISBURSED-DT\": \"21-07-2018\",\n" +
            "            \"INSTALLMENT-AMT\": \"2,335/Monthly\",\n" +
            "            \"CREDIT-LIMIT\": \"45,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"37,000\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F03\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"24\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2020-03-03 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"1,340\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": \"\",\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Jun:2020|May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|Jun:2019|May:2019|Apr:2019|Mar:2019|Feb:2019|Jan:2019|Dec:2018|Nov:2018|Oct:2018|Sep:2018|Aug:2018|Jul:2018|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|001/XXX|000/XXX|000/XXX|001/XXX|XXX/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Jun:2020|May:2020|Apr:2020|Mar:2020|Feb:2020|Jan:2020|Dec:2019|Nov:2019|Oct:2019|Sep:2019|Aug:2019|Jul:2019|\",\n" +
            "                \"VALUES\": \"11,568|11,568|11,568|11,568|13,672|15,741|17,702|19,704|21,673|23,512|23,512|27,179|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000105\",\n" +
            "            \"ACCT-NUMBER\": \"KMHPI007894532\",\n" +
            "            \"CREDIT-GRANTOR\": \"PANCHRATNA SECURITIES\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"05-04-2018\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"40,000\",\n" +
            "            \"DISBURSED-DT\": \"03-01-2017\",\n" +
            "            \"INSTALLMENT-AMT\": \"325/Weekly\",\n" +
            "            \"CREDIT-LIMIT\": \"15,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"37,000\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F01\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"52\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2017-01-10 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"1,340\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": \"\",\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|\",\n" +
            "                \"VALUES\": \"0|0|0|0|325|1,287|2,861|4,094|5,604|6,787|7,948|9,088|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"PRB0000003\",\n" +
            "            \"ACCT-NUMBER\": \"GFFJE533065659\",\n" +
            "            \"CREDIT-GRANTOR\": \"HDFC BANK LTD\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category A\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"PRB\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"31-10-2018\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"23-10-2018\",\n" +
            "            \"DISBURSED-AMT\": \"30,201\",\n" +
            "            \"DISBURSED-DT\": \"27-01-2017\",\n" +
            "            \"INSTALLMENT-AMT\": \"1,597/Monthly\",\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F03\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"24\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2018-10-23 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"3,000\",\n" +
            "            \"WRITE-OFF-AMT\": \"9,000\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|\",\n" +
            "                \"VALUES\": \"0|6,081|7,527|8,945|10,335|11,698|13,034|14,344|15,628|16,887|18,122|19,332|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000105\",\n" +
            "            \"ACCT-NUMBER\": \"RYORM729017566\",\n" +
            "            \"CREDIT-GRANTOR\": \"PANCHRATNA SECURITIES\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"03-10-2018\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": null,\n" +
            "            \"DISBURSED-AMT\": \"40,000\",\n" +
            "            \"DISBURSED-DT\": \"16-08-2016\",\n" +
            "            \"INSTALLMENT-AMT\": \"423/Weekly\",\n" +
            "            \"CREDIT-LIMIT\": \"35,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"37,000\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F01\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"104\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2018-07-17 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"1,340\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": \"\",\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|Oct:2017|Sep:2017|Aug:2017|Jul:2017|Jun:2017|May:2017|Apr:2017|Mar:2017|Feb:2017|Jan:2017|Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|XXX/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Oct:2018|Sep:2018|Aug:2018|Jul:2018|Jun:2018|May:2018|Apr:2018|Mar:2018|Feb:2018|Jan:2018|Dec:2017|Nov:2017|\",\n" +
            "                \"VALUES\": \"0|0||0|0|2,499|4,529|6,121|7,684|9,598|11,099|12,573|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"PRB0000003\",\n" +
            "            \"ACCT-NUMBER\": \"CJTTC998611440\",\n" +
            "            \"CREDIT-GRANTOR\": \"HDFC BANK LTD\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category A\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"PRB\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"31-12-2016\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"26-12-2016\",\n" +
            "            \"DISBURSED-AMT\": \"20,184\",\n" +
            "            \"DISBURSED-DT\": \"25-06-2015\",\n" +
            "            \"INSTALLMENT-AMT\": \"1,366/Monthly\",\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F03\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"18\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2016-12-26 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"1,000\",\n" +
            "            \"WRITE-OFF-AMT\": \"8,000\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|Aug:2015|Jul:2015|Jun:2015|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2016|Nov:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2016|Nov:2016|Oct:2016|Sep:2016|Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|\",\n" +
            "                \"VALUES\": \"0|1,337|2,646|3,927|5,181|6,408|7,609|8,785|9,936|11,062|12,165|13,244|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Dec:2016|Nov:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000014\",\n" +
            "            \"ACCT-NUMBER\": \"UYCGU846722774\",\n" +
            "            \"CREDIT-GRANTOR\": \"FULLERTON\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"31-08-2016\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"22-07-2016\",\n" +
            "            \"DISBURSED-AMT\": \"24,900\",\n" +
            "            \"DISBURSED-DT\": \"24-01-2015\",\n" +
            "            \"INSTALLMENT-AMT\": \"630/BiWeekly\",\n" +
            "            \"CREDIT-LIMIT\": \"24,900\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F02\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"52\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2016-07-13 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"8,999\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|Aug:2015|Jul:2015|Jun:2015|May:2015|Apr:2015|Mar:2015|Feb:2015|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2016|Jun:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|Aug:2015|\",\n" +
            "                \"VALUES\": \"0|8,650|10,220|11,250|12,250|13,230|14,190|15,130|16,500|17,400|18,270|19,120|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Jul:2016|Jun:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"PRB0000003\",\n" +
            "            \"ACCT-NUMBER\": \"SQXJW801541451\",\n" +
            "            \"CREDIT-GRANTOR\": \"HDFC BANK LTD\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category A\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"PRB\",\n" +
            "            \"ACCT-TYPE\": \"JLG GROUP\",\n" +
            "            \"REPORTED-DT\": \"30-06-2015\",\n" +
            "            \"OWNERSHIP-TYPE\": \"Primary\",\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"20-06-2015\",\n" +
            "            \"DISBURSED-AMT\": \"1,25,690\",\n" +
            "            \"DISBURSED-DT\": \"25-08-2014\",\n" +
            "            \"INSTALLMENT-AMT\": \"12,006/Monthly\",\n" +
            "            \"CREDIT-LIMIT\": null,\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F03\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"12\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"20-06-2015\",\n" +
            "            \"OVERDUE-AMT\": \"8,800\",\n" +
            "            \"WRITE-OFF-AMT\": \"10,000\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Jun:2015|May:2015|Apr:2015|Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|Oct:2014|Sep:2014|Aug:2014|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|XXX/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Jun:2015|May:2015|Apr:2015|Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|Oct:2014|Sep:2014|Aug:2014|\",\n" +
            "                \"VALUES\": \"0|34513|45533|56320|66878|77212||87327|97228|106920|116405|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"\",\n" +
            "                \"VALUES\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": null,\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": \"\",\n" +
            "                \"OWNER-NAME\": \"\",\n" +
            "                \"SECURITY-VALUATION\": \"\",\n" +
            "                \"DATE-OF-VALUATION\": \"\",\n" +
            "                \"SECURITY-CHARGE\": \"\",\n" +
            "                \"PROPERTY-ADDRESS\": \"\",\n" +
            "                \"AUTOMOBILE-TYPE\": \"\",\n" +
            "                \"YEAR-OF-MANUFACTURING\": \"\",\n" +
            "                \"REGISTRATION-NUMBER\": \"\",\n" +
            "                \"ENGINE-NUMBER\": \"\",\n" +
            "                \"CHASSIE-NUMBER\": \"\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": null,\n" +
            "            \"INCOME-FREQUENCY\": null,\n" +
            "            \"INCOME-AMOUNT\": null\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000045\",\n" +
            "            \"ACCT-NUMBER\": \"KMHPI007894532\",\n" +
            "            \"CREDIT-GRANTOR\": \"MUTHOOT\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"02-11-2016\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"16-08-2016\",\n" +
            "            \"DISBURSED-AMT\": \"25,000\",\n" +
            "            \"DISBURSED-DT\": \"28-08-2014\",\n" +
            "            \"INSTALLMENT-AMT\": \"304/Weekly\",\n" +
            "            \"CREDIT-LIMIT\": \"25,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F01\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"104\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2016-08-09 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"7,000\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|Aug:2015|Jul:2015|Jun:2015|May:2015|Apr:2015|Mar:2015|Feb:2015|Jan:2015|Dec:2014|Nov:2014|Oct:2014|Sep:2014|\",\n" +
            "                \"VALUES\": \"000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2016|Jul:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2016|Jul:2016|Jun:2016|May:2016|Apr:2016|Mar:2016|Feb:2016|Jan:2016|Dec:2015|Nov:2015|Oct:2015|Sep:2015|\",\n" +
            "                \"VALUES\": \"0|904|2,089|3,540|4,676|5,792|7,158|8,228|9,278|10,563|11,320|12,559|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2016|Jul:2016|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"mfiId\": \"MFI0000045\",\n" +
            "            \"ACCT-NUMBER\": \"BCKXI723017790\",\n" +
            "            \"CREDIT-GRANTOR\": \"MUTHOOT\",\n" +
            "            \"CREDIT-GRANTOR-GROUP\": \"Category D\",\n" +
            "            \"CREDIT-GRANTOR-TYPE\": \"MFI\",\n" +
            "            \"ACCT-TYPE\": \"JLG INDIVIDUAL\",\n" +
            "            \"REPORTED-DT\": \"05-11-2014\",\n" +
            "            \"OWNERSHIP-TYPE\": null,\n" +
            "            \"ACCOUNT-STATUS\": \"Active\",\n" +
            "            \"CLOSED-DT\": \"27-08-2014\",\n" +
            "            \"DISBURSED-AMT\": \"12,000\",\n" +
            "            \"DISBURSED-DT\": \"26-10-2013\",\n" +
            "            \"INSTALLMENT-AMT\": \"263/Weekly\",\n" +
            "            \"CREDIT-LIMIT\": \"12,000\",\n" +
            "            \"CASH-LIMIT\": null,\n" +
            "            \"CURRENT-BAL\": \"0\",\n" +
            "            \"INSTALLMENT-FREQUENCY\": \"F01\",\n" +
            "            \"ORIGINAL-TERM\": 0,\n" +
            "            \"TERM-TO-MATURITY\": 0,\n" +
            "            \"REPAYMENT-TENURE\": \"52\",\n" +
            "            \"INTEREST-RATE\": \"0.0\",\n" +
            "            \"ACTUAL-PAYMENT\": null,\n" +
            "            \"LAST-PAYMENT-DT\": \"2014-08-26 00:00:00.0\",\n" +
            "            \"OVERDUE-AMT\": \"0\",\n" +
            "            \"WRITE-OFF-AMT\": \"0\",\n" +
            "            \"PRINCIPAL-WRITE-OFF-AMT\": null,\n" +
            "            \"SETTLEMENT-AMT\": null,\n" +
            "            \"OBLIGATION\": null,\n" +
            "            \"HISTORY\": [\n" +
            "              {\n" +
            "                \"NAME\": \"COMBINED-PAYMENT-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2014|Jul:2014|Jun:2014|May:2014|Apr:2014|Mar:2014|Feb:2014|Jan:2014|Dec:2013|Nov:2013|\",\n" +
            "                \"VALUES\": \"XXX/XXX|XXX/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|000/XXX|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"HIGH-CREDIT-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2014|Jul:2014|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"CURRENT-BALANCE-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2014|Jul:2014|Jun:2014|May:2014|Apr:2014|Mar:2014|Feb:2014|Jan:2014|Dec:2013|Nov:2013|\",\n" +
            "                \"VALUES\": \"||4,991|5,930|7,078|7,976|8,855|9,930|10,772|11,186|\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"NAME\": \"AMT-PAID-HISTORY\",\n" +
            "                \"DATES\": \"Aug:2014|Jul:2014|\",\n" +
            "                \"VALUES\": \"||\"\n" +
            "              }\n" +
            "            ],\n" +
            "            \"ACCOUNT-REMARKS\": null,\n" +
            "            \"SECURITY-STATUS\": \"Un-Tagged\",\n" +
            "            \"ACCT-IN-DISPUTE\": null,\n" +
            "            \"SUIT-FILED-WILFUL-DEFAULT-STATUS\": null,\n" +
            "            \"WRITTEN-OFF-SETTLED-STATUS\": null,\n" +
            "            \"WRITE-OFF-DT\": null,\n" +
            "            \"SECURITY-DETAILS\": [\n" +
            "              {\n" +
            "                \"SECURITY-TYPE\": null,\n" +
            "                \"OWNER-NAME\": null,\n" +
            "                \"SECURITY-VALUATION\": null,\n" +
            "                \"DATE-OF-VALUATION\": null,\n" +
            "                \"SECURITY-CHARGE\": null,\n" +
            "                \"PROPERTY-ADDRESS\": null,\n" +
            "                \"AUTOMOBILE-TYPE\": null,\n" +
            "                \"YEAR-OF-MANUFACTURING\": null,\n" +
            "                \"REGISTRATION-NUMBER\": null,\n" +
            "                \"ENGINE-NUMBER\": null,\n" +
            "                \"CHASSIE-NUMBER\": null\n" +
            "              }\n" +
            "            ],\n" +
            "            \"LINKED-ACCOUNTS\": [],\n" +
            "            \"SUIT-FILED-DT\": null,\n" +
            "            \"LAST-PAID-AMOUNT\": null,\n" +
            "            \"OCCUPATION\": \"\",\n" +
            "            \"INCOME-FREQUENCY\": \"\",\n" +
            "            \"INCOME-AMOUNT\": \"\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"INQUIRY-HISTORY\": [\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"31-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"31-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"50,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"30-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"8,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"29-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,14,478\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,48,398\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,90,830\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,95,659\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,64,251\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,90,712\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"37,35,185\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,48,201\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"35,64,807\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,74,388\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,58,247\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,55,601\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,99,922\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,42,749\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,88,917\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,13,761\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,45,266\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,52,698\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,25,170\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,66,720\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,12,759\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"46,59,203\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,41,128\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,24,701\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,18,659\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,68,234\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,46,544\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,58,085\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,66,760\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,23,637\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,75,787\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,83,259\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,19,253\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,64,753\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,41,362\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"42,20,309\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"37,00,621\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"47,66,776\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,44,756\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"37,66,886\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"19,64,899\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,76,757\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,61,522\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,80,454\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,50,816\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,59,176\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,37,918\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,98,887\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,98,161\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"34,36,930\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,51,107\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,96,541\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,52,761\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,10,846\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" ;
    String stage3_3 =
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,95,963\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,28,309\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,83,240\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"47,11,693\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,98,499\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,64,474\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,40,962\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,13,689\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,85,201\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,46,364\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,49,309\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"25,21,615\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,36,156\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,93,056\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"34,58,480\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,48,186\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,30,342\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,52,507\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"27,33,143\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,38,100\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"42,30,560\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,04,580\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,40,857\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,64,805\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,39,772\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,70,435\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,89,820\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,30,023\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,91,510\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,72,729\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,85,350\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,90,799\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,88,585\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,10,710\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,47,520\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,35,632\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"46,07,328\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,60,427\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,35,764\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,71,232\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"37,96,120\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,40,574\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,23,691\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,33,740\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"37,14,832\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,30,824\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,11,321\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,89,439\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,18,126\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,64,428\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,01,685\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,45,825\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,80,680\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,37,049\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,42,220\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,14,614\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,80,939\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,33,473\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,57,413\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,69,004\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,97,520\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,15,633\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,56,301\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"19,37,269\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,45,546\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"27,56,449\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,20,839\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,98,178\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,15,321\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,00,803\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,44,738\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,52,801\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"36,95,792\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,16,733\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,37,809\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,54,615\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,17,216\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"35,92,930\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,87,709\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,70,658\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,54,221\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,34,420\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,76,196\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,31,641\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,61,232\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"26,34,423\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,16,390\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,44,370\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,52,736\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,06,014\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,60,828\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,74,577\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,99,359\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,09,089\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,65,305\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,58,838\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"34,93,379\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,36,510\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"42,26,837\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,97,397\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,17,731\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,52,666\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,67,673\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,03,595\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,46,232\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,90,138\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"25-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,33,777\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,57,614\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,35,521\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,72,300\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,01,646\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"46,92,140\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"1,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"11,15,768\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,96,138\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"46,47,877\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,76,740\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,72,736\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,90,364\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,51,718\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,60,691\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,40,774\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,38,512\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,09,763\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,25,938\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,043\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,040\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,74,824\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,045\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"26,08,389\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"20,80,176\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"34,56,834\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"7,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,17,675\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,047\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"27,87,019\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,041\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,050\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,30,811\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,78,551\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,71,894\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,60,419\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,20,387\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"47,97,732\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,00,285\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,59,031\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" ;
    String stage3_4 =
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,04,078\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,01,766\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,60,163\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,07,626\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,14,132\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"47,88,895\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"42,66,163\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,38,363\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"28,93,097\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"32,88,192\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,57,507\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"35,39,121\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"2,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"46,72,122\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,95,767\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,78,551\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,80,784\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,61,896\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,60,794\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,58,591\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,14,565\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,94,131\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,42,826\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,18,653\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,33,324\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,01,863\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,82,037\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,12,164\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"38,30,611\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,73,380\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"16,69,882\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"40,26,125\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"22,16,798\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,60,461\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"26,31,354\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"45,15,264\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,97,463\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"11,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,42,725\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"41,32,749\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"21,42,522\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,19,383\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,31,099\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,89,500\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,10,083\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,67,308\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,69,970\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,55,251\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"29,64,041\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,23,966\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"31,43,469\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"48,81,219\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,13,513\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,88,738\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"30,43,847\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,27,259\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"35,66,519\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,95,323\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"43,30,985\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"15,09,033\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"13,05,032\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"18,77,383\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"39,16,342\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,049\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"HDFC BANK LTD\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,91,579\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"23,30,231\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"2,55,836\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"24,77,295\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,005\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,50,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"33,44,085\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"7,38,601\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"4,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,021\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"8,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"5,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"17,66,915\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,003\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,006\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"49,11,237\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"0\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,008\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,029\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"47,82,050\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,004\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"9,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"6,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"12,82,091\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,023\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"44,21,549\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"35,83,313\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"3,00,048\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"1,62,887\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"23-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Personal Loan\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"14,71,180\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"PUNJAB NATIONAL BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"19-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Auto Loan (Personal)\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"PUNJAB NATIONAL BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"17-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Credit Card\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"5,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"PUNJAB NATIONAL BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"16-05-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"IDFC\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"24-02-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"OTHER\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"UTKARSH SMALL FINANCE BANK LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"22-02-2023\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"UCO BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"20-10-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Credit Card\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,00,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"UCO BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"20-10-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"MAHINDRA AND MAHINDRA FINANCIAL SERVICES LIMITED\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"29-09-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"UCO BANK\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"16-09-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Other\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"111\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"FICCL\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"12-09-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"OTHER\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"10,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"FICCL\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"22-04-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"OTHER\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"4,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"LENDER-NAME\": \"FICCL\",\n" +
            "            \"LENDER-TYPE\": \"\",\n" +
            "            \"INQUIRY-DT\": \"30-03-2022\",\n" +
            "            \"OWNERSHIP-TYPE\": \"PRIMARY\",\n" +
            "            \"CREDIT-INQ-PURPS-TYPE\": \"Credit Card\",\n" +
            "            \"CREDIT-INQUIRY-STAGE\": \"\",\n" +
            "            \"AMOUNT\": \"5,000\",\n" +
            "            \"LOAN-TYPE\": \"\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"SCORE\": [\n" +
            "          {\n" +
            "            \"NAME\": \"PERFORM CONSUMER 2.0\",\n" +
            "            \"VERSION\": null,\n" +
            "            \"VALUE\": \"676\",\n" +
            "            \"DESCRIPTION\": \"F-Low Risk\",\n" +
            "            \"FACTORS\": [\n" +
            "              {\n" +
            "                \"TYPE\": \"SF03\",\n" +
            "                \"DESC\": \"No/minimal missed payments in recent past\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"TYPE\": \"SF11\",\n" +
            "                \"DESC\": \"Normal proportion of outstanding balance to disbursed amount\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"TYPE\": \"SF25\",\n" +
            "                \"DESC\": \"Decent number of self/overall loans disbursed in the past\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"TYPE\": \"SF26\",\n" +
            "                \"DESC\": \"Considerably high number of self/overall loans disbursed in the past\"\n" +
            "              }\n" +
            "            ]\n" +
            "          },\n" +
            "          {\n" +
            "            \"NAME\": \"INCOME SEGMENT\",\n" +
            "            \"VERSION\": null,\n" +
            "            \"VALUE\": \"EX02\",\n" +
            "            \"DESCRIPTION\": \"Too few updates to rate\",\n" +
            "            \"FACTORS\": []\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"REQUESTED-SERVICES\": null,\n" +
            "      \"ACCOUNTS-SUMMARY\": {\n" +
            "        \"PRIMARY-ACCOUNTS-SUMMARY\": {\n" +
            "          \"NUMBER-OF-ACCOUNTS\": \"18\",\n" +
            "          \"ACTIVE-ACCOUNTS\": \"12\",\n" +
            "          \"OVERDUE-ACCOUNTS\": \"6\",\n" +
            "          \"SECURED-ACCOUNTS\": \"0\",\n" +
            "          \"UNSECURED-ACCOUNTS\": \"9\",\n" +
            "          \"UNTAGGED-ACCOUNTS\": \"9\",\n" +
            "          \"TOTAL-CURRENT-BALANCE\": \"184627.0\",\n" +
            "          \"CURRENT-BALANCE-SECURED\": \"0.0\",\n" +
            "          \"CURRENT-BALANCE-UNSECURED\": \"184627.0\",\n" +
            "          \"TOTAL-SANCTIONED-AMT\": \"376179.0\",\n" +
            "          \"TOTAL-DISBURSED-AMT\": \"376179.0\",\n" +
            "          \"TOTAL-AMT-OVERDUE\": \"9,020\"\n" +
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
            "          \"NUMBER-OF-ACCOUNTS\": \"1\",\n" +
            "          \"ACTIVE-ACCOUNTS\": \"1\",\n" +
            "          \"OVERDUE-ACCOUNTS\": \"1\",\n" +
            "          \"CLOSED-ACCOUNTS\": \"0\",\n" +
            "          \"NO-OF-OTHER-MFIS\": \"1\",\n" +
            "          \"NO-OF-OWN-MFIS\": \"0\",\n" +
            "          \"TOTAL-OWN-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"TOTAL-OWN-INSTALLMENT-AMT\": \"0\",\n" +
            "          \"TOTAL-OWN-DISBURSED-AMT\": \"0.0\",\n" +
            "          \"TOTAL-OWN-OVERDUE-AMT\": \"0.0\",\n" +
            "          \"TOTAL-OTHER-CURRENT-BALANCE\": \"0.0\",\n" +
            "          \"TOTAL-OTHER-INSTALLMENT-AMT\": \"12006\",\n" +
            "          \"TOTAL-OTHER-DISBURSED-AMT\": \"125690.0\",\n" +
            "          \"TOTAL-OTHER-OVERDUE-AMT\": \"8800.0\",\n" +
            "          \"MAX-WORST-DELINQUENCY\": \"0\"\n" +
            "        },\n" +
            "        \"ADDITIONAL-SUMMARY\": [\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS\",\n" +
            "            \"ATTR-VALUE\": \"7\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-GRANTORS-ACTIVE\",\n" +
            "            \"ATTR-VALUE\": \"7\"\n" +
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
            "            \"ATTR-VALUE\": \"7\"\n" +
            "          }\n" +
            "        ],\n" +
            "        \"PERFORM-ATTRIBUTES\": [\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"INQUIRIES-IN-LAST-SIX-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"304\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"LENGTH-OF-CREDIT-HISTORY-YEAR\",\n" +
            "            \"ATTR-VALUE\": \"6\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"LENGTH-OF-CREDIT-HISTORY-MONTH\",\n" +
            "            \"ATTR-VALUE\": \"2\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVERAGE-ACCOUNT-AGE-YEAR\",\n" +
            "            \"ATTR-VALUE\": \"1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVERAGE-ACCOUNT-AGE-MONTH\",\n" +
            "            \"ATTR-VALUE\": \"11\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NEW-ACCOUNTS-IN-LAST-SIX-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NEW-DELINQ-ACCOUNT-IN-LAST-SIX-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-DELINQ-INCREASE-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-CC-BALANCE-BUILDUP\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"CD-TRADES WITHIN-6-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-TIME-OPEN-TRADE\",\n" +
            "            \"ATTR-VALUE\": \"3,547\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-TRADES-GOOD-IN-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"3\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-OVERDUE-BUILDUP-SECURED-ACTIVE\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"SCORE-TRANCH-FLIP\",\n" +
            "            \"ATTR-VALUE\": \"-1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-OUTSTANDING\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-OUTSTANDING\",\n" +
            "            \"ATTR-VALUE\": \"3,15,199\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-LIVE-SECURED-ACCOUNTS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-OBLIGATIONS\",\n" +
            "            \"ATTR-VALUE\": \"3,15,199\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-SANCTION-AMOUNT-OPEN-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-SANCTION-AMOUNT-OPEN-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"2,03,900\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-SANCTION-AMOUNT-OPEN-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-SANCTION-AMOUNT-ALL-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-SANCTION-AMOUNT-ALL-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"5,49,590\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-OUTSTANDING\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-OVER-DUE-ACCOUNTS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-OVER-DUE-ACCOUNTS\",\n" +
            "            \"ATTR-VALUE\": \"7\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-OVER-DUE-ACCOUNTS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-WORST-DPD-BUCKET-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-WORST-DPD-BUCKET-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"180\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-WORST-DPD-BUCKET-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-WORST-DPD-BUCKET-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"180\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-WORST-DPD-AMOUNT-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-WORST-DPD-AMOUNT-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"39,523\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-WORST-DPD-AMOUNT-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SECURED-WORST-DPD-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-UNSECURED-WORST-DPD-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"39,523\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-WORST-DPD-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-ACTIVE-EMI\",\n" +
            "            \"ATTR-VALUE\": \"25,819\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-ACTIVE-SECURED-EMI\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-ACTIVE-UNSECURED-EMI\",\n" +
            "            \"ATTR-VALUE\": \"25,819\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-EMI\",\n" +
            "            \"ATTR-VALUE\": \"25,819\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-SECURED-EMI\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-UNSECURED-EMI\",\n" +
            "            \"ATTR-VALUE\": \"25,819\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-EMI-AMOUNT-ACTIVE\",\n" +
            "            \"ATTR-VALUE\": \"12,006\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \" MAX-EMI-AMOUNT\",\n" +
            "            \"ATTR-VALUE\": \"12,006\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-SECURED-EMI-AMOUNT\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-UNSECURED-EMI-AMOUNT\",\n" +
            "            \"ATTR-VALUE\": \"12,006\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"797\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MIN-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"-518\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-SECURED-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MIN-SECURED-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-UNSECURED-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"797\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MIN-UNSECURED-ACTIVE-ACCOUNT-AGE\",\n" +
            "            \"ATTR-VALUE\": \"-518\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-SECURED-INQUIRY-IN-LAST-6-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-UNSECURED-INQUIRY-IN-LAST-6-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUMBER-OF-LOANS-DELINQUENT-IN-LAST-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-INQUIRY-IN-LAST-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"309\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-SECURED-INQUIRY-IN-LAST-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-UNSECURED-INQUIRY-IN-LAST-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUMBER-OF-LOANS-DELINQUENT-IN-LAST-24-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"4\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-INQUIRY-IN-LAST-24-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"311\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-SECURED-INQUIRY-IN-LAST-24-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-NO-OF-UNSECURED-INQUIRY-IN-LAST-24-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-OUTSTANDING-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"1,25,690\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-SECURED-OUTSTANDING-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-UNSECURED-OUTSTANDING-AMOUNT-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"1,25,690\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"OLDEST-OPEN-CC-MON\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"YOUNGEST-OPEN-CC-MON\",\n" +
            "            \"ATTR-VALUE\": \"\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-SANCTION-AMOUNT-ALL-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"NUM-INC-BAL-REVOLVING-TRADES-12M\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-SPEND-CC-UTIL-6M\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-TIME-CC-ALL-TRADE\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-WORST-DPD-BUCKET-IN-LAST-2-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-CC-WORST-DPD-BUCKET-IN-LAST-3-YEARS\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"MAX-SANCTION-AMOUNT-ALL-ACCOUNT\",\n" +
            "            \"ATTR-VALUE\": \"1,25,690\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"TOTAL-SUM-OF-ASSETS-ACTIVE\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"CREDIT-LIMIT-TREND-12-MONTHS\",\n" +
            "            \"ATTR-VALUE\": \"27,592\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"OVERDUE-AMOUNT-TREND\",\n" +
            "            \"ATTR-VALUE\": \"12\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVG-PERCENTAGE-INCREASE-HIGH CREDIT\",\n" +
            "            \"ATTR-VALUE\": \"1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVG-PERCENTAGE-INCREASE-CB\",\n" +
            "            \"ATTR-VALUE\": \"51\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVG-PERCENTAGE-INCREASE-DPD\",\n" +
            "            \"ATTR-VALUE\": \"1\"\n" +
            "          },\n" +
            "          {\n" +
            "            \"ATTR-NAME\": \"AVG-PERCENTAGE-INCREASE-AO\",\n" +
            "            \"ATTR-VALUE\": \"0\"\n" +
            "          }\n" +
            "        ]\n" +
            "      },\n" +
            "      \"TRENDS\": {\n" +
            "        \"NAME\": \"SCORE-HISTORY\",\n" +
            "        \"DATES\": \"31-03-2023|31-12-2022|30-09-2022|30-06-2022|31-03-2022|31-12-2021|30-09-2021|30-06-2021|31-03-2021|31-12-2020|30-09-2020|31-08-2020\",\n" +
            "        \"VALUES\": \"443|749|723|703|584|584|584|584|703|690|690|680\",\n" +
            "        \"RESERVED1\": \"Perform Consumer v2\",\n" +
            "        \"RESERVED2\": null,\n" +
            "        \"RESERVED3\": null,\n" +
            "        \"DESCRIPTION\": \"K-High Risk|B-Very Low Risk|C-Very Low Risk|D-Very Low Risk|I-Medium Risk|I-Medium Risk|I-Medium Risk|I-Medium Risk|D-Very Low Risk|E-Low Risk|E-Low Risk|E-Low Risk\"\n" +
            "      },\n" +
            "      \"ALERTS\": []\n" +
            "    }\n" +
            "  }\n" +
            "}";
    @PostMapping("/api/stage2")
    public GenericResponse<CrifResponse> getStage1() throws IOException {

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

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> reportContent = objectMapper.readValue(stage3_1 + stage3_2 + stage3_3 + stage3_4, new TypeReference<Map<String, Object>>() {});

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