package com.sr.capital.external.crif.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.CommonConstant;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.RequestData;
import com.sr.capital.entity.mongo.crif.BureauInitiateModel;
import com.sr.capital.entity.mongo.crif.CrifConsentDetails;
import com.sr.capital.entity.mongo.crif.CrifReport;
import com.sr.capital.entity.mongo.crif.CrifUserModel;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.crif.Constant.Constant;
import com.sr.capital.external.crif.Constant.CrifDocumentType;
import com.sr.capital.external.crif.dto.request.*;
import com.sr.capital.external.crif.dto.response.BureauInitiateResponse;
import com.sr.capital.external.crif.dto.response.BureauQuestionnaireResponse;
import com.sr.capital.external.crif.dto.response.BureauReportResponse;
import com.sr.capital.external.crif.dto.response.CrifResponse;
import com.sr.capital.external.crif.exeception.CRIFApiException;
import com.sr.capital.external.crif.exeception.CRIFApiLimitExceededException;
import com.sr.capital.external.crif.util.CrifModelHelper;
import com.sr.capital.external.crif.util.CrifStatusCode;
import com.sr.capital.external.crif.util.CrifUserModelHelper;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final CrifModelHelper crifModelHelper;
    private final RedissonClient redissonClient;
    private final ObjectMapper mapper;
    private final CrifUserModelHelper crifUserModelHelper;
    private final CrifConsentDetailsService crifConsentDetailsService;
    @Override
    public Object initiateBureau(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CustomException, CRIFApiException, CRIFApiLimitExceededException {

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureauAndGetQuestionnaire(bureauInitiatePayloadRequest);

        if (bureauQuestionnaireResponse != null && isAuthorizedForReport(bureauQuestionnaireResponse.getStatus())) {
            return getReport(bureauQuestionnaireResponse, false);
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
    public void purgeExpiredData() {
        Pageable pageable = PageRequest.of(0, 100); // Fetch 50 records per page
        Page<CrifConsentDetails> expiredDetailsPage;
        List<String> consentIds = new ArrayList<>();

        do {
            expiredDetailsPage = getExpiredDetailsForLast24Hours(pageable);

            if (expiredDetailsPage.hasContent()) {
                List<CrifConsentDetails> expiredDetails = expiredDetailsPage.getContent();
                List<String> consentIdList = expiredDetails.stream().map(CrifConsentDetails::getConsentId).toList();

                consentIds.addAll(consentIdList);

                // Update each record's status and deletion details
                expiredDetails.forEach(e -> {
                    e.setExpirationMethod(CommonConstant.AUTOMATIC);
                    e.setStatus(CommonConstant.DELETED);
                    e.setDeletedAt(LocalDateTime.now().format(FORMATTER));
                });

                crifConsentDetailsService.saveAll(expiredDetails);
            }

            pageable = expiredDetailsPage.nextPageable();
        } while (expiredDetailsPage.hasNext());

        if (!consentIds.isEmpty()) {
            processInBatches(consentIds, 10);
        }

    }

    public void processInBatches(List<String> consentIds, int batchSize) {
        int totalSize = consentIds.size();
        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            List<String> batch = consentIds.subList(i, end);

            purgeData(batch);
        }
    }

    private void purgeData(List<String> consentIdList) {
        List<CrifReport> crifReportList = crifModelHelper.findAllByConsentId(consentIdList);
        List<CrifUserModel> crifUserModels = crifUserModelHelper.findAllByConsentId(consentIdList);
        List<BureauInitiateModel> bureauInitiateModelList = bureauInitiateModelRepo.findAllByConsentIdIn(consentIdList);

        if (crifReportList != null) {
            crifReportRepo.deleteAll(crifReportList);
        }
        if (crifUserModels != null) {
            crifUserModelHelper.deleteAll(crifUserModels);
        }
        if (bureauInitiateModelList != null) {
            bureauInitiateModelRepo.deleteAll(bureauInitiateModelList);
        }
    }

    private void deleteFromBureauInitiateModel(List<String> consentIdList) {
        List<BureauInitiateModel> bureauInitiateModelList = bureauInitiateModelRepo.findAllByConsentIdIn(consentIdList);
        if (bureauInitiateModelList != null) {
            bureauInitiateModelRepo.deleteAll(bureauInitiateModelList);
        }
    }

//    private void deleteFromCrifUserDetails(List<String> consentIdList) {
//        List<CrifUserModel> crifUserModels = crifUserModelHelper.findAllByConsentId(consentIdList);
//        if (crifUserModels != null) {
//            crifUserModelHelper.deleteAll(crifUserModels);
//        }
//    }

    private void deleteFromCrifReport(List<String> consentIdList) {
        List<CrifReport> crifReportList = crifModelHelper.findAllByConsentId(consentIdList);
        if (crifReportList != null) {
            crifReportRepo.deleteAll(crifReportList);
        }
    }

    public Page<CrifConsentDetails> getExpiredDetailsForLast24Hours(Pageable pageable) {
        // Current time
        String currentTime = LocalDateTime.now().format(FORMATTER);

        // Previous day's 12 AM
        String previousDayMidnight = LocalDateTime.now().minusDays(1).withHour(0).withMinute(0).withSecond(0).format(FORMATTER);

        return crifConsentDetailsService.findByExpiredAtBetweenAndStatus(currentTime, previousDayMidnight, pageable, Constant.ACTIVE);
    }
    @Override
    public void consentWithdrawalProcess(CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel) throws CRIFApiException {
        String consentId = deleteData(crifConsentWithdrawalRequestModel);

        CrifConsentDetails crifConsentDetails = crifConsentDetailsService.findByConsentId(consentId);
        if (crifConsentDetails != null) {
            crifConsentDetails.setExpirationMethod(CommonConstant.MANUAL);
            crifConsentDetails.setStatus(CommonConstant.DELETED);
            crifConsentDetails.setDeletedAt(LocalDateTime.now().format(FORMATTER));
            crifConsentDetailsService.save(crifConsentDetails);
        }
    }

    private String deleteData(CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel) throws CRIFApiException {
        Optional<CrifUserModel> optionalCrifUserModel = crifUserModelHelper.findByMobileDockTypeAndDocValue(crifConsentWithdrawalRequestModel.getMobile(),
                crifConsentWithdrawalRequestModel.getDocType(), crifConsentWithdrawalRequestModel.getDocValue());

        if (!optionalCrifUserModel.isPresent()) {
            throw new CRIFApiException("Invalid data");
        }
        Optional<CrifReport> optional = crifModelHelper.findByMobile(crifConsentWithdrawalRequestModel.getMobile());
        List<BureauInitiateModel> optionalBureauInitiateModel = crifModelHelper.findByMobileNumber(crifConsentWithdrawalRequestModel.getMobile());


        String consentId = optionalCrifUserModel.get().getConsentId();
        crifUserModelHelper.delete(optionalCrifUserModel.get());

        if (optionalBureauInitiateModel != null) {
            bureauInitiateModelRepo.deleteAll(optionalBureauInitiateModel);
        }

        optional.ifPresent(crifReportRepo::delete);

        return consentId;
    }

//    private String deleteFromCrifUserDetails(CrifConsentWithdrawalRequestModel crifConsentWithdrawalRequestModel) {
//        Optional<CrifUserModel> optional = crifUserModelHelper.findByMobileDockTypeAndDocValue(crifConsentWithdrawalRequestModel.getMobile(),
//                crifConsentWithdrawalRequestModel.getDocType(), crifConsentWithdrawalRequestModel.getDocValue());
//        String consentId = "";
//        if (optional.isPresent()) {
//            consentId = optional.get().getConsentId();
//            crifUserModelHelper.delete(optional.get());
//        }
//        return consentId;
//    }

//    private void deleteFromCrifReport(String mobile) {
//        Optional<CrifReport> optional = crifModelHelper.findByMobile(mobile);
//        optional.ifPresent(crifReportRepo::delete);
//    }
//    private void deleteFromBureauInitiateModel(String mobile) {
//        List<BureauInitiateModel> optional = crifModelHelper.findByMobileNumber(mobile);
//        if (optional != null) {
//            bureauInitiateModelRepo.deleteAll(optional);
//        }
//    }

    @Override
    public CrifResponse verify(BureauInitiateResponse bureauInitiateResponse) throws CustomException, CRIFApiException, CRIFApiLimitExceededException {
        CrifResponse crifResponse = CrifResponse.builder().build();

        BureauQuestionnaireResponse questionnaire = getQuestionnaire(bureauInitiateResponse);
        if (questionnaire != null && isAuthorizedForReport(questionnaire.getStatus())) {
            BureauReportResponse report = getReport(questionnaire, false);
            if (report != null) {
                Object reportData = StringUtils.covertToJsonString(report.getResult());
                setResponse(crifResponse, new HashMap<>() {{
                    put(DATA, reportData);
                    put(STAGE, STAGE_3);
                }});
            }
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
    public Map<String, Object> initiateBureauAndGetQuestionnaire(CrifVerifyOtpRequestModels crifGenerateOtpRequestModel) throws CustomException, CRIFApiException, CRIFApiLimitExceededException {
        Optional<CrifReport> optional = crifModelHelper.findByMobile(crifGenerateOtpRequestModel.getMobile());
        if (optional.isPresent()) {
            if (isOldRequest(optional.get().getValidTill())) {
                return getStoredReport(optional.get());
            } else {
                //Refresh report
                return getReport(crifGenerateOtpRequestModel.getMobile());
                }
        }

        BureauInitiatePayloadRequest bureauInitiatePayloadRequest = BureauInitiatePayloadRequest.builder()
                .firstName(crifGenerateOtpRequestModel.getFirstName())
                .lastName(crifGenerateOtpRequestModel.getLastName())
                .mobile(crifGenerateOtpRequestModel.getMobile())
                .email(crifGenerateOtpRequestModel.getEmail()).build();

        setDocTypeValue(bureauInitiatePayloadRequest, crifGenerateOtpRequestModel.getDocType(), crifGenerateOtpRequestModel.getDocValue());

        BureauQuestionnaireResponse bureauQuestionnaireResponse = initiateBureauAndGetQuestionnaire(bureauInitiatePayloadRequest);

        if (bureauQuestionnaireResponse != null && isAuthorizedForReport(bureauQuestionnaireResponse.getStatus())) {
            BureauReportResponse report = getReport(bureauQuestionnaireResponse, false);
            if (report != null) {
                return new HashMap<>() {{
                    put(DATA, StringUtils.covertToJsonString(report.getResult())); put(STAGE, STAGE_3);
                }};
            }
        }
        return new HashMap<>(){{put(DATA, bureauQuestionnaireResponse); put(STAGE, STAGE_2);}};
    }

    private HashMap<String, Object> getReport(String mobile) throws CRIFApiException, CRIFApiLimitExceededException {
        List<BureauInitiateModel> bureauInitiateModelList = crifModelHelper.
                findByMobileNumber(mobile);
        if (bureauInitiateModelList != null && !bureauInitiateModelList.isEmpty()) {
            BureauQuestionnaireResponse bureauQuestionnaireResponse = new BureauQuestionnaireResponse();
            BureauInitiateModel bureauInitiateModel = bureauInitiateModelList.get(0);
            bureauQuestionnaireResponse.setOrderId(bureauInitiateModel.getOrderId());
            bureauQuestionnaireResponse.setReportId(bureauInitiateModel.getReportId());
            bureauQuestionnaireResponse.setRedirectURL(bureauInitiateModel.getRedirectUrl());

            BureauReportResponse report = getReport(bureauQuestionnaireResponse, true);
            if (report != null) {
                return new HashMap<>() {{
                    put(DATA, StringUtils.covertToJsonString(report));
                    put(STAGE, STAGE_3);
                }};
            }
        }

        return null;
    }

    private Map<String, Object> getStoredReport(CrifReport crifGenerateOtpRequestModel) {
        String consentId = crifGenerateOtpRequestModel.getConsentId();
        updateConsentHistory(consentId);
        return new HashMap<>(){{put(DATA, StringUtils.covertToJsonString(crifGenerateOtpRequestModel.getResult())); put(STAGE, STAGE_3);}};
    }

    private void updateConsentHistory(String consentId) {
        CrifConsentDetails crifConsentDetails = crifConsentDetailsService.findByConsentId(consentId);
        if (crifConsentDetails != null) {
            List<String> consentDateHistory = crifConsentDetails.getConsentDateHistory();
            consentDateHistory.add(LocalDateTime.now().format(FORMATTER));
            crifConsentDetails.setConsentDateHistory(consentDateHistory);
            crifConsentDetailsService.save(crifConsentDetails);
        }
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

    private BureauReportResponse getReport(BureauQuestionnaireResponse questionnaire, boolean isRefreshRequest) throws CRIFApiException, CRIFApiLimitExceededException {
        BureauReportPayloadRequest bureauReportPayloadRequest = BureauReportPayloadRequest.builder()
                .reportId(questionnaire.getReportId())
                .orderId(questionnaire.getOrderId())
                .redirectURL(questionnaire.getRedirectURL())
                .build();
        return getReport(bureauReportPayloadRequest, isRefreshRequest);
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
    public BureauQuestionnaireResponse initiateBureauAndGetQuestionnaire(BureauInitiatePayloadRequest bureauInitiatePayloadRequest) throws CRIFApiException, CRIFApiLimitExceededException {

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
        return webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage1Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                header, null,
                        requestPayload, BureauInitiateResponse.class);
    }

    private void saveInitiateData(BureauInitiatePayloadRequest bureauInitiatePayloadRequest, BureauInitiateResponse bureauInitiateResponse, String requestPayload, HttpHeaders header) {
        List<BureauInitiateModel> optional = crifModelHelper.findByMobileNumber(bureauInitiatePayloadRequest.getMobile());
        BureauInitiateModel bureauInitiateModel;
        if (optional != null && !optional.isEmpty()) {
            bureauInitiateModel = optional.get(0);
            bureauInitiateModel.setInitResponse(bureauInitiateResponse.toString());
            bureauInitiateModel.setInitStatus(bureauInitiateResponse.getStatus());
            bureauInitiateModel.setOrderId(bureauInitiateResponse.getOrderId());
            bureauInitiateModel.setReportId(bureauInitiateResponse.getReportId());
            bureauInitiateModel.setRequestHeader(getHeadersAsString(header));
            bureauInitiateModel.setInitRequestPayload(requestPayload);
            bureauInitiateModel.setSrCompanyId(RequestData.getTenantId());
        } else {
            String consentIdByMobileNumber = getConsentIdByMobileNumber(bureauInitiatePayloadRequest.getMobile());
            bureauInitiateModel = BureauInitiateModel.builder()
                    .redirectUrl(bureauInitiateResponse.getRedirectURL())
                    .initStatus(bureauInitiateResponse.getStatus())
                    .orderId(bureauInitiateResponse.getOrderId())
                    .reportId(bureauInitiateResponse.getReportId())
                    .requestHeader(getHeadersAsString(header))
                    .initRequestPayload(requestPayload)
                    .mobile(bureauInitiatePayloadRequest.getMobile())
                    .srCompanyId(RequestData.getTenantId())
                    .initResponse(bureauInitiateResponse.toString())
                    .consentId(consentIdByMobileNumber)
                    .build();
        }
        bureauInitiateModelRepo.save(bureauInitiateModel);
    }

    private String getConsentIdByMobileNumber(String mobile) {
        Optional<CrifUserModel> byMobileNumber = crifUserModelHelper.findByMobile(mobile);
        if (byMobileNumber.isPresent()) {
            return byMobileNumber.get().getConsentId();
        }
        return "";
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

        header.add(ORDER_ID, orderId);
        header.add(ACCESS_TOKEN, getAccessCode());
        header.add(APP_ID, appProperties.getCrifAppId());
//        header.add(Constant.CONTENT_TYPE, TEXT_PLANE);
        header.add(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE); // Set explicitly
        header.add(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
        header.add(MERCHANT_ID, appProperties.getCrifCustomerId());
        return header;
    }

    private HttpHeaders getHeaderForQuestionnaire(String orderId, String reportId, boolean isAuthentication, boolean isRefreshRequest) {
        HttpHeaders header = new HttpHeaders();
        header.add(ACCESS_TOKEN, getAccessCode());
        header.add(APP_ID, appProperties.getCrifAppId());
        header.add(CONTENT_TYPE, TEXT_PLANE);
        header.add(MERCHANT_ID, appProperties.getCrifCustomerId());
        if (isAuthentication) {
            header.add(REQUEST_TYPE, AUTHORIZATION);
        }
        if (isRefreshRequest) {
            header.add(REQUEST_TYPE, UPGRADE);
        }
        header.add(REPORT_ID, reportId);
        header.add(HttpHeaders.ACCEPT, MediaType.TEXT_PLAIN_VALUE); // Set explicitly
        header.add(ORDER_ID, orderId);

        return header;
    }


    @Override
    public BureauQuestionnaireResponse getQuestionnaire(BureauInitiateResponse bureauInitiateResponse) throws CRIFApiException, CRIFApiLimitExceededException {

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



    public BureauQuestionnaireResponse authenticateQuestion(BureauQuestionnairePayloadRequest bureauQuestionnairePayloadRequest) throws CRIFApiException, CRIFApiLimitExceededException {

        HttpHeaders header = getHeaderForQuestionnaire(bureauQuestionnairePayloadRequest.getOrderId(),
                bureauQuestionnairePayloadRequest.getReportId(), true, false);

        bureauQuestionnairePayloadRequest.setAccessCode(header.get(Constant.ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauQuestionnairePayloadRequest);

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

            saveQuestionnaireResponseIntoDb(bureauQuestionnaireResponse, requestPayload);

            handleExceptions(bureauQuestionnaireResponse.getStatus());

        } else {
            throw new CRIFApiException("External API returned null");
        }

        return bureauQuestionnaireResponse;
    }

    private void saveQuestionnaireResponseIntoDb(BureauQuestionnaireResponse bureauQuestionnaireResponse, String requestPayload) {
        Optional<BureauInitiateModel> optional = bureauInitiateModelRepo.
                findByReportIdAndOrderId(bureauQuestionnaireResponse.getReportId(),
                        bureauQuestionnaireResponse.getOrderId());
        if (optional.isPresent()) {
            BureauInitiateModel bureauInitiateModel = optional.get();
            bureauInitiateModel.setLastQuestion(bureauQuestionnaireResponse.getQuestion());

            Map<String, List<String>> questionsOptionMap = new HashMap<>();
            if (bureauQuestionnaireResponse.getQuestion() != null && bureauQuestionnaireResponse.getOptionList() != null) {
                questionsOptionMap.put(bureauQuestionnaireResponse.getQuestion(), bureauQuestionnaireResponse.getOptionList());
            }
            List<Map<String, List<String>>> optionList = bureauInitiateModel.getQuestionOptionList();
            if (!questionsOptionMap.isEmpty()) {
                if (optionList != null) {
                    optionList.add(questionsOptionMap);
                } else {
                    optionList = Collections.singletonList(questionsOptionMap);
                }
            }

            bureauInitiateModel.setQuestionOptionList(optionList);
            bureauInitiateModel.setQuestionnaireRequestPayload(requestPayload);
            bureauInitiateModel.setButtonBehavior(bureauQuestionnaireResponse.getButtonBehaviour());
            bureauInitiateModel.setQuestionnaireStatus(bureauQuestionnaireResponse.getStatus());
            bureauInitiateModel.setStatusDesc(bureauQuestionnaireResponse.getStatusDesc());
            bureauInitiateModel.setCompletedAt(bureauQuestionnaireResponse.getCompletedAt());
            bureauInitiateModel.setQuestionnaireResponse(bureauQuestionnaireResponse.toString());
            bureauInitiateModelRepo.save(bureauInitiateModel);
        }
    }

        private void handleExceptions(String statusCode) throws CRIFApiException, CRIFApiLimitExceededException {
                CrifStatusCode crifStatusCode = CrifStatusCode.fromCode(statusCode);

                switch (crifStatusCode) {
                    case S00,S03, S04, S05, S07, S08, S09:
                        throw new CRIFApiException(crifStatusCode.getDescription() + " " + statusCode);

                    case UNAUTHORIZED:
                        throw new SecurityException(crifStatusCode.getDescription());
                    case S02:
                        throw new CRIFApiLimitExceededException(crifStatusCode.getDescription());
                }

        }

    @Override
    public BureauReportResponse getReport(BureauReportPayloadRequest bureauReportPayloadRequest, boolean isRefreshRequest) throws CRIFApiException, CRIFApiLimitExceededException {
        setDummyData(bureauReportPayloadRequest);
        updateStaticDataForReport(bureauReportPayloadRequest);
        return generateReport(bureauReportPayloadRequest, isRefreshRequest);
    }

    private BureauReportResponse generateReport(BureauReportPayloadRequest bureauReportPayloadRequest, boolean isRefreshRequest) throws CRIFApiException, CRIFApiLimitExceededException {
        HttpHeaders header = getHeaderForQuestionnaire(bureauReportPayloadRequest.getOrderId(),
                bureauReportPayloadRequest.getReportId(), false, isRefreshRequest);

        bureauReportPayloadRequest.setAccessCode(header.get(ACCESS_TOKEN).get(0));
        String requestPayload = StringUtils.toPipeSeparatedString(bureauReportPayloadRequest);

        String orderId = header.get(ORDER_ID).get(0);

        Object bureauReportResponse = webClientUtil.makeExternalCallBlocking(ServiceName.CRIF, appProperties.getCrifBaseUri(), appProperties.getCrifExtractStage2Endpoint(),
                        HttpMethod.POST, ServiceName.CRIF.getName(),
                        header, null,
                        requestPayload, Object.class);

        if (bureauReportResponse != null) {
            log.info("response {} ", bureauReportResponse);

            CrifReport crifReport = saveReportData(orderId, bureauReportResponse, bureauReportPayloadRequest, isRefreshRequest);

            if (bureauReportResponse instanceof LinkedHashMap<?,?> map && map.containsKey("status")) {
                handleExceptions((String) map.get("Status"));
            }

            return BureauReportResponse.builder()
                    .result(crifReport.getResult())
                    .orderId(crifReport.getOrderId())
                    .reportId(crifReport.getReportId())
                    .build();
        } else {
            throw new CRIFApiException("External API returned null");
        }
    }

    private CrifReport saveReportData(String orderId, Object bureauReportResponse,
                                      BureauReportPayloadRequest bureauReportPayloadRequest, boolean isRefreshRequest) {

        Map<String, String> mobileAndNumberByOrderIdAndReportId =
                getMobileAndNumberByOrderIdAndReportId(orderId, bureauReportPayloadRequest.getReportId());

        String mobile = mobileAndNumberByOrderIdAndReportId.get(MOBILE);
        String consent = mobileAndNumberByOrderIdAndReportId.get(CONSENT);

        CrifReport crifReport;

        if (isRefreshRequest) {
            updateConsent(consent);
            crifReport = buildCrifReport(mobile, consent, bureauReportResponse, bureauReportPayloadRequest);
        } else {
            Optional<CrifReport> existingReport = crifModelHelper.findByMobile(mobile);
            crifReport = existingReport.orElseGet(() ->
                    buildCrifReport(mobile, consent , bureauReportResponse, bureauReportPayloadRequest)
            );
        }

        crifModelHelper.save(crifReport);
        return crifReport;
    }

    private CrifReport buildCrifReport(String mobile, String consent, Object bureauReportResponse,
                                       BureauReportPayloadRequest bureauReportPayloadRequest) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            byte[] byteArrayResponse = objectMapper.writeValueAsBytes(bureauReportResponse);
            return CrifReport.builder()
                    .mobile(mobile)
                    .consentId(consent)
                    .result(Base64.getEncoder().encodeToString(byteArrayResponse))
                    .orderId(bureauReportPayloadRequest.getOrderId())
                    .reportId(bureauReportPayloadRequest.getReportId())
                    .srCompanyId(RequestData.getTenantId())
                    .validTill(StringUtils.getTimeAfterMonths(1))
                    .build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete old data for given consent id noe=w this consent id will treat as new id because old is deleted
     * @param consentId
     */
    private void updateConsent(String consentId) {

        deleteFromBureauInitiateModelViaSettingNull(consentId);
        deleteFromCrifReport(Collections.singletonList(consentId));

    }

    private void deleteFromBureauInitiateModelViaSettingNull(String consentId) {
        List<BureauInitiateModel> bureauInitiateModelList = bureauInitiateModelRepo.findAllByConsentIdIn(Collections.singletonList(consentId));
            if (bureauInitiateModelList != null && !bureauInitiateModelList.isEmpty()) {
                bureauInitiateModelList.forEach(d -> {
                    d.setLastQuestion(null);
                    d.setInitStatus(null);
                    d.setInitRequestPayload(null);
                    d.setButtonBehavior(null);
                    d.setInitResponse(null);
                    d.setQuestionnaireRequestPayload(null);
                    d.setQuestionnaireResponse(null);
                    d.setQuestionnaireStatus(null);
                    d.setQuestionOptionList(null);
                    d.setRedirectUrl(null);
                    d.setRequestHeader(null);
                    d.setUserAnswer(null);
                });
                bureauInitiateModelRepo.saveAll(bureauInitiateModelList);
            }
    }

    private Map<String, String> getMobileAndNumberByOrderIdAndReportId(String orderId, String reportId) {
        Optional<BureauInitiateModel> bureauInitiateModel = bureauInitiateModelRepo.findByReportIdAndOrderId(reportId, orderId);
        Map<String, String> res = new HashMap<>();
        if (bureauInitiateModel.isPresent()) {
            res.put(MOBILE, bureauInitiateModel.get().getMobile());
            res.put(CONSENT, bureauInitiateModel.get().getConsentId());
        }
        return res;
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