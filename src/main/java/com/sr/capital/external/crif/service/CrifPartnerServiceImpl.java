package com.sr.capital.external.crif.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.Constant.CrifDocumentType;
import com.sr.capital.external.crif.dto.request.*;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.util.CrifReportModelHelper;
import com.sr.capital.external.crif.util.CrifStatusCode;
import com.sr.capital.external.crif.util.StringUtils;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.repository.mongo.BureauInitiateModelRepo;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.sr.capital.external.crif.Constant.Constant.*;
import static com.sr.capital.external.crif.service.CrifOtpServiceImpl.setResponse;
import static com.sr.capital.external.crif.util.CrifStatusCode.S01;
import static com.sr.capital.external.crif.util.CrifStatusCode.S10;
import static com.sr.capital.external.crif.util.StringUtils.FORMATTER;

@Service
@Slf4j
@RequiredArgsConstructor
public class CrifPartnerServiceImpl implements CrifPartnerService {

    private final WebClientUtil webClientUtil;
    private final AppProperties appProperties;
    private final BureauInitiateModelRepo bureauInitiateModelRepo;
    private final CrifReportRepo crifReportRepo;
    private final CrifReportModelHelper crifReportModelHelper;
    private final RedissonClient redissonClient;
    private final ObjectMapper mapper;
    @Override
    public Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CustomException, CRIFApiException {

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureauAndGetQuestionnaire(bureauInitiatePayloadRequest);

        if (bureauQuestionnaireResponse != null && isAuthorizedForReport(bureauQuestionnaireResponse.getStatus())) {
            return getReport(bureauQuestionnaireResponse);
        }

        return bureauQuestionnaireResponse;
    }

    @Override
    public Map<String, String> getDocType() {
        Map<String, String> map = CrifDocumentType.getAllDocumentTypes().stream()
                .collect(Collectors
                        .toMap(CrifDocumentType::getDisplayName,
                                Enum::name, (a, b) -> b, LinkedHashMap::new));
        return map;
    }

    @Override
    public CrifResponse verify(BureauInitiateResponse bureauInitiateResponse) throws CustomException, CRIFApiException {
        CrifResponse crifResponse = CrifResponse.builder().build();

        BureauQuestionnaireResponse questionnaire = getQuestionnaire(bureauInitiateResponse);
        if (questionnaire != null && isAuthorizedForReport(questionnaire.getStatus())) {
            setResponse(crifResponse, new HashMap<>(){{put(DATA, getReport(questionnaire)); put(STAGE, STAGE_3);}});
            return crifResponse;
        }
        setResponse(crifResponse, new HashMap<>(){{put(DATA, questionnaire); put(STAGE, STAGE_2);}});
        return crifResponse;
    }



    boolean isAuthorizedForReport(String status) throws CustomException {
        if (status.equals(CrifStatusCode.S02.name())) {
            throw new CustomException("You have exceed the limit");
        }
        return status.equals(S01.name()) || status.equals(S10.name());
    }
    @Override
    public Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException, CRIFApiException {
        Optional<CrifReport> optional = crifReportModelHelper.findByMobile(crifGenerateOtpRequestModel.getMobile());
        if (optional.isPresent() && isOldRequest(optional.get().getValidTill())) {
            return getStoredReport(optional.get());
        }

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

    private Map<String, Object> getStoredReport(CrifReport crifGenerateOtpRequestModel) {
        return new HashMap<>(){{put(DATA, crifGenerateOtpRequestModel.getResult()); put(STAGE, STAGE_3);}};
    }

    private boolean isOldRequest(String time) {
        if (time != null) {
            LocalDateTime storedTime = LocalDateTime.parse(time, FORMATTER);

            // Get the current time
            LocalDateTime currentTime = LocalDateTime.now();

            // Check if the current time is less than the stored time
            return currentTime.isBefore(storedTime);
        }
        return false;
    }

    private BureauReportResponse getReport(BureauQuestionnaireResponse questionnaire) throws CRIFApiException {
        BureauReportPayloadRequest bureauReportPayloadRequest = BureauReportPayloadRequest.builder()
                .reportId(questionnaire.getReportId())
                .orderId(questionnaire.getOrderId())
                .redirectURL(questionnaire.getRedirectURL())
                .build();
        return getReport(bureauReportPayloadRequest);
    }

    private void setDocTypeValue(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, String docType, String docValue) {
        CrifDocumentType crifDocumentType = CrifDocumentType.valueOf(docType);
        switch (crifDocumentType) {
            case PAN: {
                bureauInitiatePayloadRequest.setPan(docValue);
                break;
            }
            case VOTER: {
                bureauInitiatePayloadRequest.setVoterId(docValue);
                break;
            }
            case DRIVING_LICENCE: {
                bureauInitiatePayloadRequest.setDl(docValue);
                break;

            }
            case PASSPORT: {
                bureauInitiatePayloadRequest.setPassport(docValue);
                break;
            }
            case RATION_CARD: {
                bureauInitiatePayloadRequest.setRationCard(docValue);
                break;
            }
            case UID: {
                bureauInitiatePayloadRequest.setUID(docValue);
                break;
            }
            case OTHER: {
                bureauInitiatePayloadRequest.setOtherId1(docValue);
                break;
            }
            case CKYC: {
                bureauInitiatePayloadRequest.setCkyc(docValue);
                break;
            }
            case NREGA: {
                bureauInitiatePayloadRequest.setNrega(docValue);
                break;
            }
        }
    }

    @Override
    public BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CRIFApiException {

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

            saveInitiateData(bureauInitiatePayloadRequest, bureauInitiateResponse, requestPayload, header);

            String statusCode = bureauInitiateResponse.getStatus();
            if (!statusCode.equals(CrifStatusCode.S06.name())) {
                throw new CRIFApiException("External API returned an error",
                        statusCode,
                        statusCode.equals("401") ? CrifStatusCode.UNAUTHORIZED.getDescription() :
                                CrifStatusCode.fromCode(statusCode).getDescription());
            } else {
                //get Questions List
                questionnaire = getQuestionnaire(bureauInitiateResponse);
            }
        } else {
            throw new CRIFApiException("External API returned null");
        }

       return questionnaire;
    }

    private BureauInitiateResponse authenticateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, String requestPayload, String orderId, HttpHeaders header) {
        //        setDummyData(bureauInitiatePayloadRequest);


//        log.info("OrderId: {}, RequestPayload: {}, Headers: {}, BaseUrl: {}, EndPoint: {}",
//                orderId,
//                requestPayload,
//                header.entrySet().stream()
//                        .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
//                        .collect(Collectors.joining(", ")),
//                appProperties.getCrifBaseUri(),
//                appProperties.getCrifExtractStage2Endpoint());

        return webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage1Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                header, null,
                        requestPayload, BureauInitiateResponse.class);
    }

    private void saveInitiateData(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, BureauInitiateResponse bureauInitiateResponse, String requestPayload, HttpHeaders header) {
        BureauInitiateModel bureauInitiateModel = BureauInitiateModel.builder()
                .redirectUrl(bureauInitiateResponse.getRedirectURL())
                .initStatus(bureauInitiateResponse.getStatus())
                .orderId(bureauInitiateResponse.getOrderId())
                .reportId(bureauInitiateResponse.getReportId())
                .requestHeader(getHeadersAsString(header))
                .requestPayload(requestPayload)
                .mobile(bureauInitiatePayloadRequest.getMobile())
                .srCompanyId(RequestData.getTenantId())
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
    public BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException {

        BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest = updateStaticData(bureauInitiateResponse);

        if (bureauQuestionnairePayloadRequest != null && bureauInitiateResponse.getUserAnswer() != null) {
            bureauQuestionnairePayloadRequest.setUserAnswer(bureauInitiateResponse.getUserAnswer());
        }

            return authenticateQuestion(bureauQuestionnairePayloadRequest);
    }

    private BureauQuestionnairePayloadRequest updateStaticData(BureauInitiateResponse bureauInitiateResponse) {
        return BureauQuestionnairePayloadRequest
                .builder()
                .reportId(bureauInitiateResponse.getReportId() != null ?
                        bureauInitiateResponse.getReportId() : "CCR220808CR373632334")
                .accessCode(bureauInitiateResponse.getAccessCode() != null ?
                        bureauInitiateResponse.getAccessCode(): getAccessCode())
                .orderId(bureauInitiateResponse.getOrderId() != null ?
                        bureauInitiateResponse.getOrderId() : getOrderId())
                .redirectURL(bureauInitiateResponse.getRedirectURL() != null && !bureauInitiateResponse.getRedirectURL().isEmpty()?
                        bureauInitiateResponse.getRedirectURL() : "https://cir.crifhighmark.com/Inquiry/B2B/secureService.action")
                .paymentFlag("N")
                .reportFlag("Y")
                .alertFlag("N")
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



    public BureauQuestionnaireResponse authenticateQuestion(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) throws CRIFApiException {

        HttpHeaders header = getHeaderForQuestionnaire(bureauQuestionnairePayloadRequest.getOrderId(),
                bureauQuestionnairePayloadRequest.getReportId(), true);

        bureauQuestionnairePayloadRequest.setAccessCode(header.get(Constant.ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauQuestionnairePayloadRequest);

//        String orderId = header.get(Constant.ORDER_ID).get(0);

//        log.info("OrderId: {}, RequestPayload: {}, Headers: {}, BaseUrl: {}, EndPoint: {}",
//                orderId,
//                requestPayload,
//                header.entrySet().stream()
//                        .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
//                        .collect(Collectors.joining(", ")),
//                appProperties.getCrifBaseUri(),
//                appProperties.getCrifExtractStage2Endpoint());

        Object object = webClientUtil.makeExternalCallBlocking(ServiceName.CRIF,
                appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                HttpMethod.POST, ServiceName.CRIF.getName(),
                header, null,
                requestPayload, Object.class);

        BureauQuestionnaireResponse bureauQuestionnaireResponse = null;

        bureauQuestionnaireResponse = mapper.convertValue(object, BureauQuestionnaireResponse.class);

        if (bureauQuestionnaireResponse != null) {
            log.info("status {} , reportId {}, orderId {}, redirectUrl {}, Question {}, buttonBehavior {}, optionList {}",
                    bureauQuestionnaireResponse.getStatus(),
                    bureauQuestionnaireResponse.getReportId(), bureauQuestionnaireResponse.getOrderId(),
                    bureauQuestionnaireResponse.getRedirectURL(), bureauQuestionnaireResponse.getQuestion(),
                    bureauQuestionnaireResponse.getButtonBehaviour(), bureauQuestionnaireResponse.getOptionList());
            Optional<BureauInitiateModel> optional = bureauInitiateModelRepo.
                    findByReportIdAndOrderId(bureauQuestionnaireResponse.getReportId(),
                    bureauQuestionnaireResponse.getOrderId());
            if (optional.isPresent()) {
                BureauInitiateModel bureauInitiateModel = optional.get();
                bureauInitiateModel.setQuestion(bureauQuestionnaireResponse.getQuestion());
                bureauInitiateModel.setOptionList(bureauQuestionnaireResponse.getOptionList());
                bureauInitiateModel.setButtonBehavior(bureauQuestionnaireResponse.getButtonBehaviour());
                bureauInitiateModel.setQuestionnaireStatus(bureauQuestionnaireResponse.getStatus());
                bureauInitiateModel.setStatusDesc(bureauQuestionnaireResponse.getStatusDesc());
                bureauInitiateModel.setCompletedAt(bureauQuestionnaireResponse.getCompletedAt());
                bureauInitiateModel.setQuestionnaireResponse(bureauQuestionnaireResponse.toString());
                bureauInitiateModelRepo.save(bureauInitiateModel);
            }
            String statusCode = bureauQuestionnaireResponse.getStatus();
            if (statusCode.equals(CrifStatusCode.S08.name())) {
                throw new CRIFApiException("External API returned an error",
                        statusCode,
                        statusCode.equals("401") ? CrifStatusCode.UNAUTHORIZED.getDescription() :
                                CrifStatusCode.fromCode(statusCode).getDescription());
            }
        } else {
            throw new CRIFApiException("External API returned null");
        }

        return bureauQuestionnaireResponse;
    }

    @Override
    public BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest) throws CRIFApiException {
        setDummyData(bureauReportPayloadRequest);
        updateStaticDataForReport(bureauReportPayloadRequest);
        return generateReport(bureauReportPayloadRequest);
    }

    private BureauReportResponse generateReport(BureauReportPayloadRequest bureauReportPayloadRequest) throws CRIFApiException {
        HttpHeaders header = getHeaderForQuestionnaire(bureauReportPayloadRequest.getOrderId(),
                bureauReportPayloadRequest.getReportId(), false);

        bureauReportPayloadRequest.setAccessCode(header.get(Constant.ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauReportPayloadRequest);

        String orderId = header.get(Constant.ORDER_ID).get(0);

//        log.info("OrderId: {}, RequestPayload: {}, Headers: {}, BaseUrl: {}, EndPoint: {}",
//                orderId,
//                requestPayload,
//                header.entrySet().stream()
//                        .map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue()))
//                        .collect(Collectors.joining(", ")),
//                appProperties.getCrifBaseUri(),
//                appProperties.getCrifExtractStage2Endpoint());

        Object bureauReportResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                        header, null,
                        requestPayload, Object.class);

        if (bureauReportResponse != null) {
            log.info("response {} ", bureauReportResponse);

            String mobile = getMobileNumberByOrderIdAndReportId(orderId, bureauReportPayloadRequest.getReportId());
            CrifReport crifReport = CrifReport.builder()
                    .orderId(bureauReportPayloadRequest.getOrderId())
                    .result(bureauReportResponse)
                    .reportId(bureauReportPayloadRequest.getReportId())
                    .srCompanyId(RequestData.getTenantId())
                    .mobile(mobile)
                    .validTill(StringUtils.getTimeAfterOneMonths())
                    .build();

            crifReportRepo.save(crifReport);

            return BureauReportResponse.builder()
                    .result(crifReport.getResult())
                    .orderId(crifReport.getOrderId())
                    .reportId(crifReport.getReportId())
                    .build();
        } else {
            throw new CRIFApiException("External API returned null");
        }
    }

    private String getMobileNumberByOrderIdAndReportId(String orderId, String reportId) {
        Optional<BureauInitiateModel> bureauInitiateModel = bureauInitiateModelRepo.findByReportIdAndOrderId(reportId, orderId);
        if (bureauInitiateModel.isPresent()) {
            return bureauInitiateModel.get().getMobile();
        }
        return "";
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