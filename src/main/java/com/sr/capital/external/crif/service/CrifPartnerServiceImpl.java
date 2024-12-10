package com.sr.capital.external.crif.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import com.sr.capital.entity.mongo.crif.BureauQuestionnaireModel;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.dto.request.*;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.util.StatusCode;
import com.sr.capital.external.crif.util.StringUtils;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.repository.mongo.BureauInitiateModelRepo;
import com.sr.capital.repository.mongo.BureauQuestionnaireModelRepo;
import com.sr.capital.repository.mongo.CrifReportRepo;
import com.sr.capital.util.Base64Util;
import com.sr.capital.util.ProviderConfigUtil;
import com.sr.capital.util.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.sr.capital.external.crif.Constant.Constant.*;

@Service
@Slf4j
public class CrifPartnerServiceImpl implements CrifPartnerService {

    private final WebClientUtil webClientUtil;
    private final AppProperties appProperties;
    private final BureauInitiateModelRepo bureauInitiateModelRepo;
    private final BureauQuestionnaireModelRepo bureauQuestionnaireModelRepo;
    private final CrifReportRepo crifReportRepo;


    public CrifPartnerServiceImpl(ProviderConfigUtil providerConfigUtil, WebClientUtil webClientUtil,
                                  AppProperties appProperties, BureauInitiateModelRepo bureauInitiateModelRepo,
                                  BureauQuestionnaireModelRepo bureauQuestionnaireModelRepo, CrifReportRepo crifReportRepo) {
        this.webClientUtil = webClientUtil;
        this.appProperties = appProperties;
        this.bureauInitiateModelRepo = bureauInitiateModelRepo;
        this.bureauQuestionnaireModelRepo = bureauQuestionnaireModelRepo;
        this.crifReportRepo = crifReportRepo;
    }

    @Override
    public Map<String, Object> initiateBureau(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) {
        BureauInitiatePayloadRequest bureauInitiatePayloadRequest = BureauInitiatePayloadRequest.builder()
                .firstName(crifGenerateOtpRequestModel.getFirstName())
                .lastName(crifGenerateOtpRequestModel.getLastName())
                .mobile(crifGenerateOtpRequestModel.getMobile())
                .email(crifGenerateOtpRequestModel.getEmail()).build();

        setDocTypeValue(bureauInitiatePayloadRequest, crifGenerateOtpRequestModel.getDocType(), crifGenerateOtpRequestModel.getDocValue());

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureau(bureauInitiatePayloadRequest);
        if (bureauQuestionnaireResponse != null && (bureauQuestionnaireResponse.getStatus().equals("S01") ||
                bureauQuestionnaireResponse.getStatus().equals("S10"))) {
            return new HashMap<>(){{put(DATA, getReport(bureauQuestionnaireResponse)); put(STAGE, STAGE_3);}};
        }
        return new HashMap<>(){{put(DATA, bureauQuestionnaireResponse); put(STAGE, STAGE_2);}};
    }

    private BureauReportResponse getReport(BureauQuestionnaireResponse questionnaire) {
        BureauReportPayloadRequest bureauReportPayloadRequest = BureauReportPayloadRequest.builder()
                .reportId(questionnaire.getReportId())
                .orderId(questionnaire.getOrderId())
                .redirectURL(questionnaire.getRedirectURL())
                .build();
        return getReport(bureauReportPayloadRequest);
    }

    private void setDocTypeValue(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, String docType, String docValue) {
        switch (docType) {
            case PAN : {
                bureauInitiatePayloadRequest.setPan(docValue);
                break;
            }
            case VOTER_ID: {
                bureauInitiatePayloadRequest.setVoterId(docValue);
                break;
            }
            case DRIVING_LICENSE: {
                bureauInitiatePayloadRequest.setDl(docValue);
                break;

            }
            case PASSPORT: {
                bureauInitiatePayloadRequest.setPassport(docValue);
                break;
            }
        }
    }

    @Override
    public BureauQuestionnaireResponse initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) {


//        setDummyData(bureauInitiatePayloadRequest);
        updateStaticData(bureauInitiatePayloadRequest);
        String orderId = getOrderId();
        HttpHeaders header = getHeaderForInitiateBureau(orderId);

        String requestPayload = StringUtils.toPipeSeparatedString(bureauInitiatePayloadRequest);

        log.info("OrderId :{} requestPayload: {}", orderId, requestPayload);
        header.keySet().forEach(log::info);
        header.values().forEach(v -> v.forEach(log::info));
        log.info("BaseUrl {}, EndPoint {}", appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint());

        BureauInitiateResponse bureauInitiateResponse = null;
        CompletableFuture<BureauInitiateResponse> response = CompletableFuture.completedFuture(
                webClientUtil.makeExternalCall(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage1Endpoint(),
                        HttpMethod.POST,
                        header, null,
                        requestPayload, BureauInitiateResponse.class)
        );

        try {
            bureauInitiateResponse = response.get();
            log.info("tatus {} , reportId {}, orderId {}, result {}, data {}", bureauInitiateResponse.getStatus(),
                    bureauInitiateResponse.getReportId(), bureauInitiateResponse.getOrderId(),
                    bureauInitiateResponse.getResult(), bureauInitiateResponse.getData());
        } catch (InterruptedException | ExecutionException e) {
            try {
                log.error("error :: {}", e);
                log.error("error while converting {}", response.get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        BureauQuestionnaireResponse questionnaire = null;

        if (bureauInitiateResponse != null) {
            saveInitiateData(bureauInitiateResponse, requestPayload, header);

            String statusCode = bureauInitiateResponse.getStatus();
            if (!statusCode.equals(StatusCode.S06.name())) {
                throw new CRIFApiException("External API returned an error",
                        statusCode,
                        statusCode.equals("401") ? StatusCode.UNAUTHORIZED.getDescription() :
                                StatusCode.fromCode(statusCode).getDescription());
            } else {
                //get Questions List
                questionnaire = getQuestionnaire(bureauInitiateResponse);
            }
        }

       return questionnaire;
    }

    private void saveInitiateData(BureauInitiateResponse bureauInitiateResponse, String requestPayload, HttpHeaders header) {
        BureauInitiateModel<Object> bureauInitiateModel = BureauInitiateModel.builder()
                .redirectUrl(bureauInitiateResponse.getRedirectURL())
                .statusCode(bureauInitiateResponse.getStatus())
                .orderId(bureauInitiateResponse.getOrderId())
                .reportId(bureauInitiateResponse.getReportId())
                .requestHeader(getHeadersAsString(header))
                .requestPayload(requestPayload)
                .details(bureauInitiateResponse.toString())
                .build();

        bureauInitiateModelRepo.save(bureauInitiateModel);
    }


    public String getHeadersAsString(HttpHeaders headers) {
        if (headers != null) {
            return headers.toSingleValueMap().entrySet().stream()
                    .map(entry -> entry.getKey() + ": " + entry.getValue())  // Format key-value pair
                    .collect(Collectors.joining("\n"));  // Join with newlines
        }
        return "";
    }

    private void updateStaticData(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) {
        bureauInitiatePayloadRequest.setCustomerId(appProperties.getCrifCustomerId());
        bureauInitiatePayloadRequest.setProductId(appProperties.getCrifProductCode());
        bureauInitiatePayloadRequest.setConsent("Y"); //Todo : verify
    }


    private String getOrderId() {
        return appProperties.getCrifCustomerId() + "_" + System.currentTimeMillis();
    }

    private void setDummyData(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) {
        bureauInitiatePayloadRequest.setFirstName("T");
        bureauInitiatePayloadRequest.setLastName("KANNAN");
        bureauInitiatePayloadRequest.setDOB("28-05-1965");
        bureauInitiatePayloadRequest.setMobile("6673196819");
        bureauInitiatePayloadRequest.setEmail("abc@abc.com");
        bureauInitiatePayloadRequest.setPan("NBLPF6801L");
        bureauInitiatePayloadRequest.setVoterId("CA38352681758583");
        bureauInitiatePayloadRequest.setAddress1("M/S KABILESWARAN LORRY BOOKING OFFICE, 43B, MADURAI - MANDAPAM ROAD, MANAMADURAI TIRUPUVANAM SIVAGANGA");
        bureauInitiatePayloadRequest.setVillage1("TIRUPUVANAM");
        bureauInitiatePayloadRequest.setCity1("TIRUPUVANAM");
        bureauInitiatePayloadRequest.setState1("TN");
        bureauInitiatePayloadRequest.setCountry1("India");
        bureauInitiatePayloadRequest.setPin1("630606");

    }

    @Override
    public String getAccessCode() {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata"));
        Date now = new Date();
        String timestamp = sdf.format(now);

        AccessCodeDto.AccessCodeDtoBuilder accessCodeDtoBuilder = AccessCodeDto.builder().userId(appProperties.getCrifUser())
                .customerId(appProperties.getCrifCustomerId())
                .productCode(appProperties.getCrifProductCode())
                .password(appProperties.getCrifPassword())
                .dateTimeStamp(timestamp);
        String pipeSeparatedString = StringUtils.toPipeSeparatedString(accessCodeDtoBuilder);
        return Base64Util.encode(pipeSeparatedString.substring(0, pipeSeparatedString.length() - 1));
    }

    private HttpHeaders getHeaderForInitiateBureau(String orderId) {
        HttpHeaders header = new HttpHeaders();

        header.add(Constant.ORDER_ID, orderId);
        header.add(Constant.ACCESS_TOKEN, getAccessCode());
        header.add(Constant.APP_ID, appProperties.getCrifAppId());
//        header.add(Constant.CONTENT_TYPE, TEXT_PLANE);
        header.add(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE); // Set explicitly
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        header.add(Constant.MERCHANT_ID, appProperties.getCrifCustomerId());
        return header;
    }

    private HttpHeaders getHeaderForQuestionnaire(String orderId, String reportId, boolean isAuthentication) {
        HttpHeaders header = new HttpHeaders();
        header.add(Constant.ACCESS_TOKEN, getAccessCode());
        header.add(Constant.APP_ID, appProperties.getCrifAppId());
        header.add(Constant.CONTENT_TYPE, TEXT_PLANE);
        header.add(Constant.MERCHANT_ID, appProperties.getCrifCustomerId());
        if (isAuthentication) {
            header.add(Constant.REQUEST_TYPE, Constant.AUTHORIZATION);
        }
        header.add(Constant.REPORT_ID, reportId);
        header.add(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE); // Set explicitly
        header.add(Constant.ORDER_ID, orderId);

        return header;
    }


    @Override
    public BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse) {
        BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest = BureauQuestionnairePayloadRequest
                .builder()
                .reportFlag("N")
                .reportId(bureauInitiateResponse.getReportId() != null ?
                        bureauInitiateResponse.getReportId() : "CCR220808CR373632334")
                .accessCode(bureauInitiateResponse.getAccessCode() != null ?
                        bureauInitiateResponse.getAccessCode(): getAccessCode())
                .orderId(bureauInitiateResponse.getOrderId() != null ?
                        bureauInitiateResponse.getOrderId() : getOrderId())
                .redirectURL(bureauInitiateResponse.getRedirectURL() != null ?
                        bureauInitiateResponse.getRedirectURL() : "https://cir.crifhighmark.com/Inquiry/B2B/secureService.action")
                .paymentFlag("N")
                .build();

        if (bureauInitiateResponse.getUserAnswer() != null) {
            bureauQuestionnairePayloadRequest.setUserAnswer(bureauInitiateResponse.getUserAnswer());
        }

        return authenticateQuestion(bureauQuestionnairePayloadRequest);
    }

    private void updateStaticData(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) {
        bureauQuestionnairePayloadRequest.setPaymentFlag("N");
    }

    private void setDummyData(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) {
        bureauQuestionnairePayloadRequest.setAccessCode(getAccessCode());
        bureauQuestionnairePayloadRequest.setOrderId("ATest0114");
        bureauQuestionnairePayloadRequest.setReportId("CCR220808CR373632334");
        bureauQuestionnairePayloadRequest.setRedirectURL("https://cir.crifhighmark.com/Inquiry/B2B/secureService.action");
        bureauQuestionnairePayloadRequest.setUserAnswer("Y");
        bureauQuestionnairePayloadRequest.setReportFlag("N");
    }


    @Override
    public BureauQuestionnaireResponse authenticationQuestionnaire(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) {
        setDummyData(bureauQuestionnairePayloadRequest);
        updateStaticData(bureauQuestionnairePayloadRequest);
        return authenticateQuestion(bureauQuestionnairePayloadRequest);
    }


    public BureauQuestionnaireResponse authenticateQuestion(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) {

        HttpHeaders header = getHeaderForQuestionnaire(bureauQuestionnairePayloadRequest.getOrderId(),
                bureauQuestionnairePayloadRequest.getReportId(), true);

        bureauQuestionnairePayloadRequest.setAccessCode(header.get(Constant.ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauQuestionnairePayloadRequest);

        String orderId = header.get(Constant.ORDER_ID).get(0);

        log.info("OrderId :{} requestPayload: {}", orderId, requestPayload);
        header.keySet().forEach(log::info);
        header.values().forEach(v -> v.forEach(log::info));
        log.info("BaseUrl {}, EndPoint {}", appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint());

        CompletableFuture<BureauQuestionnaireResponse> completableFuture = CompletableFuture.completedFuture(
                webClientUtil.makeExternalCall(ServiceName.FLEXI, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                        HttpMethod.POST,
                        header, null,
                        requestPayload, BureauQuestionnaireResponse.class)
        );

        BureauQuestionnaireResponse bureauQuestionnaireResponse = null;
        try {
            bureauQuestionnaireResponse = completableFuture.get();
            log.info("status {} , reportId {}, orderId {}, result {}, redirectUrl {}, Question {}, buttonBehavior {}, optionList {}",
                    bureauQuestionnaireResponse.getStatus(),
                    bureauQuestionnaireResponse.getReportId(), bureauQuestionnaireResponse.getOrderId(),
                    bureauQuestionnaireResponse.getResult(), bureauQuestionnaireResponse.getRedirectURL(), bureauQuestionnaireResponse.getQuestion(),
                    bureauQuestionnaireResponse.getButtonBehavior(), bureauQuestionnaireResponse.getOptionList());
        } catch (InterruptedException | ExecutionException e) {
            try {
                log.error("error :: {}", e);
                log.error("error while converting {}", completableFuture.get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }

        if (bureauQuestionnaireResponse != null) {
            BureauQuestionnaireModel bureauQuestionnaireModel = BureauQuestionnaireModel.builder()
                    .accessCode(bureauQuestionnaireResponse.getAccessCode())
                    .reportId(bureauQuestionnaireResponse.getReportId())
                    .question(bureauQuestionnaireResponse.getQuestion())
                    .redirectUrl(bureauQuestionnaireResponse.getRedirectURL())
                    .orderId(bureauQuestionnaireResponse.getOrderId())
                    .optionList(bureauQuestionnaireResponse.getOptionList())
                    .buttonBehavior(bureauQuestionnaireResponse.getButtonBehavior())
                    .status(bureauQuestionnaireResponse.getStatus())
                    .statusDesc(bureauQuestionnaireResponse.getStatusDesc())
                    .completedAt(bureauQuestionnaireResponse.getCompletedAt())
                    .result(bureauQuestionnaireResponse.toString()).build();

            bureauQuestionnaireModelRepo.save(bureauQuestionnaireModel);
        }
        return bureauQuestionnaireResponse;
    }

    @Override
    public BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest) {
        setDummyData(bureauReportPayloadRequest);
        updateStaticDataForReport(bureauReportPayloadRequest);
        return generateReport(bureauReportPayloadRequest);
    }

    private BureauReportResponse generateReport(BureauReportPayloadRequest bureauReportPayloadRequest) {
        HttpHeaders header = getHeaderForQuestionnaire(bureauReportPayloadRequest.getOrderId(),
                bureauReportPayloadRequest.getReportId(), false);

        bureauReportPayloadRequest.setAccessCode(header.get(Constant.ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauReportPayloadRequest);

        String orderId = header.get(Constant.ORDER_ID).get(0);

        log.info("OrderId :{} requestPayload: {}", orderId, requestPayload);
        header.keySet().forEach(log::info);
        header.values().forEach(v -> v.forEach(log::info));
        log.info("BaseUrl {}, EndPoint {}", appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint());

        CompletableFuture<Object> completableFuture = CompletableFuture.completedFuture(
                webClientUtil.makeExternalCall(ServiceName.FLEXI, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                        HttpMethod.POST,
                        header, null,
                        requestPayload, Object.class)
        );

        Object bureauReportResponse = null;
        try {
            bureauReportResponse = completableFuture.get();
            log.info("response {} ", bureauReportResponse);
        } catch (InterruptedException | ExecutionException e) {
            try {
                log.error("error :: {}", e);
                log.error("error while converting {}", completableFuture.get());
            } catch (InterruptedException | ExecutionException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }


        CrifReport crifReport = CrifReport.builder()
                .orderId(bureauReportPayloadRequest.getOrderId())
                .result(bureauReportResponse)
                .reportId(bureauReportPayloadRequest.getReportId())
                .build();
        
        crifReportRepo.save(crifReport);
        
        //        BureauQuestionnaireModel bureauQuestionnaireModel = BureauQuestionnaireModel.builder()
//                        .orderId(bureauReportPayloadRequest.getOrderId())
//                                .reportId(bureauReportPayloadRequest.getReportId())
//                                        .result(bureauReportResponse.getResult()).build();
//        bureauQuestionnaireModelRepo.save(bureauQuestionnaireModel);

        return BureauReportResponse.builder()
                .result(crifReport.getResult())
                .orderId(crifReport.getOrderId())
                .reportId(crifReport.getReportId())
                .build();
    }

    private void setDummyData(BureauReportPayloadRequest bureauReportPayloadRequest) {
        bureauReportPayloadRequest.setAccessCode(getAccessCode());
//        bureauReportPayloadRequest.setOrderId("ATest0114");
//        bureauReportPayloadRequest.setReportId("CCR220808CR373632334");
//        bureauReportPayloadRequest.setRedirectURL("https://cir.crifhighmark.com/Inquiry/B2B/secureService.action");

    }

    private void updateStaticDataForReport(BureauReportPayloadRequest bureauReportPayloadRequest) {
        bureauReportPayloadRequest.setReportFlag("Y");
        bureauReportPayloadRequest.setAlertFlag("N");
        bureauReportPayloadRequest.setPaymentFlag("N");
        bureauReportPayloadRequest.setJsonFlag("Y");
    }

}