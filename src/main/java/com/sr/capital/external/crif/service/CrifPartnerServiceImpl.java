package com.sr.capital.external.crif.service;

import com.sr.capital.config.AppProperties;
import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.dto.request.*;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.util.StatusCode;
import com.sr.capital.external.crif.util.StringUtils;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.repository.mongo.BureauInitiateModelRepo;
import com.sr.capital.repository.mongo.BureauQuestionnaireModelRepo;
import com.sr.capital.repository.mongo.CrifReportRepo;
import com.sr.capital.util.Base64Util;
import com.sr.capital.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sr.capital.external.crif.Constant.Constant.*;
import static com.sr.capital.external.crif.util.StatusCode.S01;
import static com.sr.capital.external.crif.util.StatusCode.S10;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrifPartnerServiceImpl implements CrifPartnerService {

    private final WebClientUtil webClientUtil;
    private final AppProperties appProperties;
    private final BureauInitiateModelRepo bureauInitiateModelRepo;
    private final BureauQuestionnaireModelRepo bureauQuestionnaireModelRepo;
    private final CrifReportRepo crifReportRepo;
    private final RedissonClient redissonClient;

    @Override
    public Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) {

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureauAndGetQuestionnaire(bureauInitiatePayloadRequest);

        if (bureauQuestionnaireResponse != null && isAuthorizedForReport(bureauQuestionnaireResponse.getStatus())) {
            return getReport(bureauQuestionnaireResponse);
        }

        return bureauQuestionnaireResponse;
    }

    @Override
    public Object verify(BureauInitiateResponse bureauInitiateResponse) {
        BureauQuestionnaireResponse questionnaire = getQuestionnaire(bureauInitiateResponse);
        if (questionnaire != null && isAuthorizedForReport(questionnaire.getStatus())) {
            return getReport(questionnaire);
        }
        return questionnaire;
    }



    boolean isAuthorizedForReport(String status) {
        return status != null && (status.equals(S01.name()) || status.equals(S10.name()));
    }
    @Override
    public Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) {
        BureauInitiatePayloadRequest bureauInitiatePayloadRequest = BureauInitiatePayloadRequest.builder()
                .firstName(crifGenerateOtpRequestModel.getFirstName())
                .lastName(crifGenerateOtpRequestModel.getLastName())
                .mobile(crifGenerateOtpRequestModel.getMobile())
                .email(crifGenerateOtpRequestModel.getEmail()).build();

        setDocTypeValue(bureauInitiatePayloadRequest, crifGenerateOtpRequestModel.getDocType(), crifGenerateOtpRequestModel.getDocValue());

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureauAndGetQuestionnaire(bureauInitiatePayloadRequest);

        if (bureauQuestionnaireResponse != null && isAuthorizedForReport(bureauQuestionnaireResponse.getStatus())) {
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
    public BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) {

        updateStaticData(bureauInitiatePayloadRequest);

        String requestPayload = StringUtils.toPipeSeparatedString(bureauInitiatePayloadRequest);

        String orderId = getOrderId();

        HttpHeaders header = getHeaderForInitiateBureau(orderId);

        BureauInitiateResponse bureauInitiateResponse = authenticateBureau(bureauInitiatePayloadRequest, requestPayload,
                orderId, header);


        BureauQuestionnaireResponse questionnaire = null;

        if (bureauInitiateResponse != null) {
            log.info("status {} , reportId {}, orderId {}, data {}", bureauInitiateResponse.getStatus(),
                    bureauInitiateResponse.getReportId(), bureauInitiateResponse.getOrderId(),
                    bureauInitiateResponse.getData());

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

    private BureauInitiateResponse authenticateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, String requestPayload, String orderId, HttpHeaders header) {
        //        setDummyData(bureauInitiatePayloadRequest);


        log.info("OrderId :{} requestPayload: {}", orderId, requestPayload);
        header.keySet().forEach(log::info);
        header.values().forEach(v -> v.forEach(log::info));
        log.info("BaseUrl {}, EndPoint {}", appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint());

        return webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage1Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                header, null,
                        requestPayload, BureauInitiateResponse.class);
    }

    private void saveInitiateData(BureauInitiateResponse bureauInitiateResponse, String requestPayload, HttpHeaders header) {
        BureauInitiateModel bureauInitiateModel = BureauInitiateModel.builder()
                .redirectUrl(bureauInitiateResponse.getRedirectURL())
                .initStatus(bureauInitiateResponse.getStatus())
                .orderId(bureauInitiateResponse.getOrderId())
                .reportId(bureauInitiateResponse.getReportId())
                .requestHeader(getHeadersAsString(header))
                .requestPayload(requestPayload)
                .initResponse(bureauInitiateResponse.toString())
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

        RMapCache<String, String> crifAccessToken = redissonClient
                .getMapCache(Constants.RedisKeys.CRIF_ACCESS_TOKEN);

        String key = crifAccessToken.get(Constants.RedisKeys.CRIF_ACCESS_TOKEN);
        if (key != null) {
        return key;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

            sdf.setTimeZone(TimeZone.getTimeZone(ASIA_KOLKATA));
            Date now = new Date();
            String timestamp = sdf.format(now);

            AccessCodeDto.AccessCodeDtoBuilder accessCodeDtoBuilder = AccessCodeDto.builder().userId(appProperties.getCrifUser())
                    .customerId(appProperties.getCrifCustomerId())
                    .productCode(appProperties.getCrifProductCode())
                    .password(appProperties.getCrifPassword())
                    .dateTimeStamp(timestamp);
            String pipeSeparatedString = StringUtils.toPipeSeparatedString(accessCodeDtoBuilder);

            String encode = Base64Util.encode(pipeSeparatedString.substring(0, pipeSeparatedString.length() - 1));
            crifAccessToken.put(Constants.RedisKeys.CRIF_ACCESS_TOKEN, encode, 25, TimeUnit.MINUTES);
            return encode;
        }
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

        BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest = updateStaticData(bureauInitiateResponse);

        if (bureauQuestionnairePayloadRequest != null && bureauInitiateResponse.getUserAnswer() != null) {
            bureauQuestionnairePayloadRequest.setUserAnswer(bureauInitiateResponse.getUserAnswer());
        }

            return authenticateQuestion(bureauQuestionnairePayloadRequest);
    }

    private BureauQuestionnairePayloadRequest updateStaticData(BureauInitiateResponse bureauInitiateResponse) {
        return BureauQuestionnairePayloadRequest
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

        BureauQuestionnaireResponse bureauQuestionnaireResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                HttpMethod.POST, ServiceName.CRIF.getName(),
                header, null,
                requestPayload, BureauQuestionnaireResponse.class);



        if (bureauQuestionnaireResponse != null) {
            log.info("status {} , reportId {}, orderId {}, redirectUrl {}, Question {}, buttonBehavior {}, optionList {}",
                    bureauQuestionnaireResponse.getStatus(),
                    bureauQuestionnaireResponse.getReportId(), bureauQuestionnaireResponse.getOrderId(),
                    bureauQuestionnaireResponse.getRedirectURL(), bureauQuestionnaireResponse.getQuestion(),
                    bureauQuestionnaireResponse.getButtonBehavior(), bureauQuestionnaireResponse.getOptionList());
            Optional<BureauInitiateModel> optional = bureauInitiateModelRepo.
                    findByReportIdAndOrderId(bureauQuestionnaireResponse.getReportId(),
                    bureauQuestionnaireResponse.getOrderId());
            if (optional.isPresent()) {
                BureauInitiateModel bureauInitiateModel = optional.get();
                BureauInitiateModel bureauQuestionnaireModel = bureauInitiateModel.toBuilder()
                        .question(bureauQuestionnaireResponse.getQuestion())
                        .optionList(bureauQuestionnaireResponse.getOptionList())
                        .buttonBehavior(bureauQuestionnaireResponse.getButtonBehavior())
                        .questionnaireStatus(bureauQuestionnaireResponse.getStatus())
                        .statusDesc(bureauQuestionnaireResponse.getStatusDesc())
                        .completedAt(bureauQuestionnaireResponse.getCompletedAt())
                        .questionnaireResponse(bureauQuestionnaireResponse.toString()).build();
                bureauInitiateModelRepo.save(bureauQuestionnaireModel);
            }
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

        Object bureauReportResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                        header, null,
                        requestPayload, Object.class);


            log.info("response {} ", bureauReportResponse);

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