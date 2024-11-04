package com.sr.capital.external.flexi.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.omunify.encryption.algorithm.AES256;
import com.sr.capital.config.AppProperties;
import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.request.CreateLeadRequestDto;
import com.sr.capital.dto.request.LoanMetaDataDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.entity.mongo.CreditPartnerConfig;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.external.common.GenericCreditPartnerService;
import com.sr.capital.external.common.request.DocumentUploadRequestDto;
import com.sr.capital.external.dto.response.ValidateLoanDetailsResponse;
import com.sr.capital.external.flexi.dto.response.*;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import com.sr.capital.helpers.enums.ProviderResponseTemplateType;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.repository.mongo.CreditPartnerConfigRepository;
import com.sr.capital.repository.primary.BaseCreditPartnerRepository;
import com.sr.capital.spine.JsonPathEvaluator;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.ProviderConfigUtil;
import com.sr.capital.util.ProviderHelperUtil;
import com.sr.capital.util.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class FlexiPartnerService extends GenericCreditPartnerService {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private BaseCreditPartnerRepository baseCreditPartnerRepository;

    @Autowired
    private CreditPartnerConfigRepository creditPartnerConfigRepository;

    @Autowired
    private JsonPathEvaluator jsonPathEvaluator;

    @Autowired
    private ProviderHelperUtil providerHelperUtil;

    @Autowired
    private ProviderConfigUtil providerConfigUtil;

    @Autowired
    private AES256 aes256;

    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClientUtil webClientUtil;
    @Override
    public AccessTokenResponseDto getAccessToken(String partner) {
        RMapCache<String, AccessTokenResponseDto> accessTokenInfo = redissonClient
                .getMapCache(Constants.RedisKeys.ACCESS_TOKEN);

        AccessTokenResponseDto responseDto = accessTokenInfo.get(partner);

        if (responseDto == null) {
            BaseCreditPartner partnerInfo = getPartnerInfo(partner);

            CreditPartnerConfig partnerConfig = creditPartnerConfigRepository
                    .findByPartnerId(partnerInfo.getId());
            CreditPartnerConfig.decryptInfo(partnerConfig, aes256);

            responseDto = getAccessTokenResponseDto(partner, partnerConfig, partnerInfo, accessTokenInfo);

        }
        return responseDto;
    }

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode)
            throws InvalidVendorCodeException, InvalidVendorTokenException {
        if(!appProperties.getFlexiVendorCode().equalsIgnoreCase(vendorCode)){
            throw new InvalidVendorCodeException();
        }

        if(!appProperties.getFlexiVendorToken().equalsIgnoreCase(vendorToken)){
            throw new InvalidVendorTokenException();
        }
        return true;
    }

    @Override
    public ValidateLoanDetailsResponse validateLoanDetails(LoanMetaDataDto loanMetaDataDto) {

        ValidateLoanDetailsResponse validateLoanDetailsResponse =null;

        HttpResponse<?> restResponseEntity = null;

        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.VALIDATE_LOAN.name(),ProviderRequestTemplateType.VALIDATE_LOAN.name(),null);

        try {

            String url = MessageFormat.format((String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), ""),loanMetaDataDto.getValidateLoanData().getMobileNumber());

            restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    url,
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.VALIDATE_TOKEN_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            validateLoanDetailsResponse = MapperUtils.convertValue(response.getData(),
                    ValidateLoanDetailsResponse.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return validateLoanDetailsResponse;
    }


    public LoanDetails getLoanDetails(LoanMetaDataDto loanMetaDataDto) {

        LoanDetails loanDetails =null;

        HttpResponse<?> restResponseEntity = null;

        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.GET_LOAN.name(),ProviderRequestTemplateType.GET_LOAN.name(),null);

        try {
            restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    (String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), ""),
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.GET_LOAN_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            loanDetails = MapperUtils.convertValue(response.getData(),
                    LoanDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return loanDetails;

    }


    @Override
    public Object uploadDocument(LoanMetaDataDto loanMetaDataDto) {

        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.UPLOAD_DOCUMENT.name(),ProviderRequestTemplateType.UPLOAD_DOCUMENT.name(),null);
        String responseDto =null;
         log.info("[uploadDocument] start request {} ",loanMetaDataDto);
        DocumentUploadRequestDto documentUploadRequestDto = loanMetaDataDto.getDocumentUploadRequestDtos();
            try {
                RMapCache<String, Boolean> documentCacheDetails = redissonClient
                        .getMapCache(documentUploadRequestDto.getKey());
                HttpHeaders fileHeaders = new HttpHeaders();
                fileHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
               // HttpEntity<InputStream> fileEntity = new HttpEntity<>(documentUploadRequestDto.getInputStream(), fileHeaders);

                InputStreamResource fileResource = new InputStreamResource(documentUploadRequestDto.getInputStream()) {
                    @Override
                    public String getFilename() {
                        // Provide a default file name if needed
                        return documentUploadRequestDto.getFileName();
                    }
                };
                MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
                body.add("file",fileResource);
                body.add("loan_code",loanMetaDataDto.getLoanId());
                body.add("document_type",documentUploadRequestDto.getDocumentType());
                body.add("document_category",documentUploadRequestDto.getDocumentCategory());
                body.add("metadata",documentUploadRequestDto.getMetaData());
                HttpHeaders httpHeaders =new HttpHeaders();
                Map<String,String> headerParams = (Map<String, String>) loanMetaDataDto.getParams().get(ProviderUrlConfigTypes.HEADER.name());
                if(headerParams!=null){
                    headerParams.forEach((k,v)->{
                        httpHeaders.add(k, String.valueOf(v));
                    });
                }
                 responseDto = webClientUtil.makeExternalCallBlocking(ServiceName.FLEXI,loanMetaDataDto.getParams().get(ProviderUrlConfigTypes.BASE_URL.name()).toString(),null, HttpMethod.POST,loanMetaDataDto.getLoanVendorName(),httpHeaders, (Map<String, String>) loanMetaDataDto.getParams().get(ProviderUrlConfigTypes.QUERY_PARAM.name()),body,String.class);
                log.info("[uploadDocument] response dto {} ",responseDto);
                documentCacheDetails.put(documentUploadRequestDto.getKey(), true,15, TimeUnit.MINUTES);
            }catch (Exception ex){
                log.error("error in ducument upload {} for partner {} , loanId {} ",ex.getMessage(),loanMetaDataDto.getLoanVendorName(), loanMetaDataDto.getLoanId());

                log.error("error in ducument upload {} ",ex);

            }




        return responseDto;
    }

    @Override
    public Object getPendingDocuments(LoanMetaDataDto loanMetaDataDto) {

        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.PENDING_DOCUMENT.name(),ProviderRequestTemplateType.PENDING_DOCUMENT.name(), PendingDocumentResponseDto.class);
        HttpResponse<?> restResponseEntity = null;
        PendingDocumentResponseDto pendingDocumentResponseDto =null;
        try {

            String url = (String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), "");

            if(loanMetaDataDto.getPostSanction()){
                Map<String, Object> queryParams = (Map<String, Object>) loanMetaDataDto.getParams().get(ProviderUrlConfigTypes.QUERY_PARAM.name());
                queryParams.put("list_type","post_sanction");
            }
            restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    url,
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.VALIDATE_TOKEN_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            pendingDocumentResponseDto = MapperUtils.convertValue(response.getData(),
                    PendingDocumentResponseDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return pendingDocumentResponseDto;
    }

    @Override
    public Object fetchDisburmentDetails(LoanMetaDataDto loanMetaDataDto) {
        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.FETCH_DISBURSED_DETAILS.name(),ProviderRequestTemplateType.FETCH_DISBURSED_DETAILS.name(), DisbursmentDetails.class);
        HttpResponse<?> restResponseEntity = null;
        DisbursmentDetails disbursmentDetails =null;
        try {

         String url =   MessageFormat.format((String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), ""),loanMetaDataDto.getLoanId());
         restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    url,
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.DISBURSEMENT_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            disbursmentDetails = MapperUtils.convertValue(response.getData(),
                    DisbursmentDetails.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return disbursmentDetails;
    }

    @Override
    public Object createLead(String partner, CreateLeadRequestDto requestDto) {
        updateCustomInfoForCreateLead(requestDto);
       return super.createLead(partner,requestDto);
    }



    @Override
    public Object fetchSanctionDetails(LoanMetaDataDto loanMetaDataDto) {
        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.FETCH_SANCTIONED_OFFER.name(),ProviderRequestTemplateType.FETCH_SANCTIONED_OFFER.name(), SanctionResponseDto.class);
        HttpResponse<?> restResponseEntity = null;
        SanctionResponseDto sanctionResponseDto =null;
        try {

            String url = (String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), "");

            restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    url,
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.SANCTION_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            sanctionResponseDto = MapperUtils.convertValue(response.getData(),
                    SanctionResponseDto.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sanctionResponseDto;
    }

    @Override
    public Object acceptOffer(LoanMetaDataDto loanMetaDataDto) {

        buildMetadata(loanMetaDataDto,ProviderRequestTemplateType.ACCEPT_SECTION_OFFER.name(),ProviderRequestTemplateType.ACCEPT_SECTION_OFFER.name(), SanctionResponseDto.class);

        HttpResponse<?> restResponseEntity = null;
        AcceptSanctionOffer acceptSanctionOffer =null;
        try {

            String url = (String) loanMetaDataDto.getParams().getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), "");

            restResponseEntity = providerHelperUtil.makeApiCall(loanMetaDataDto.getParams(),
                    url,
                    loanMetaDataDto.getExternalRequestBody(),
                    null);
        } catch (UnirestException | URISyntaxException e) {
            log.error(loanMetaDataDto.getLoanVendorName(), e);
        }

        GenericResponse<?> response = new GenericResponse<>();

        providerHelperUtil.setResponse(response, restResponseEntity,
                ProviderResponseTemplateType.ACCEPT_OFFER_RESPONSE.name(),loanMetaDataDto.getLoanVendorId());

        try {
            acceptSanctionOffer = MapperUtils.convertValue(response.getData(),
                    AcceptSanctionOffer.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return acceptSanctionOffer;
    }



    private AccessTokenResponseDto getAccessTokenResponseDto(String partner, CreditPartnerConfig partnerConfig, BaseCreditPartner partnerInfo, RMapCache<String, AccessTokenResponseDto> accessTokenInfo) {
        AccessTokenResponseDto responseDto = null;
        AccessTokenRequestDto requestDto = MapperUtils.mapClass(partnerConfig, AccessTokenRequestDto.class);
        requestDto.setPartner(partner);

        Map<String, Object> params = providerConfigUtil.getUrlAndQueryParam(partnerInfo.getId(),
                requestDto.getMetaData(),
                ProviderRequestTemplateType.GET_TOKEN.name());
        MultiValueMap<String,String> formdata = new LinkedMultiValueMap<>();
      formdata.add("grant_type","client_credentials");
      formdata.add("scopes","*");


      HttpHeaders httpHeaders =new HttpHeaders();
      Map<String,String> headerParams = (Map<String, String>) params.get(ProviderUrlConfigTypes.HEADER.name());
        if(headerParams!=null){
            headerParams.forEach((k,v)->{
                httpHeaders.add(k, String.valueOf(v));
            });
        }
        com.sr.capital.external.flexi.dto.response.AccessTokenResponseDto responseDto1 = webClientUtil.makeExternalCallBlocking(ServiceName.FLEXI,params.get(ProviderUrlConfigTypes.BASE_URL.name()).toString(),null, HttpMethod.POST,partner,httpHeaders, (Map<String, String>) params.get(ProviderUrlConfigTypes.QUERY_PARAM.name()),formdata, com.sr.capital.external.flexi.dto.response.AccessTokenResponseDto.class);


        if(responseDto1!=null){
            responseDto =AccessTokenResponseDto.builder().accessToken("Bearer "+responseDto1.getAccessToken()).expiry(String.valueOf(responseDto1.getExpiresIn())).build();
        }

        accessTokenInfo.put(partner, responseDto, responseDto1.getExpiresIn()*partnerConfig.getExpiryMultiplier(), TimeUnit.MILLISECONDS);
        return responseDto;
    }



    private void buildMetadata(LoanMetaDataDto loanMetaDataDto, String providerTemplateName, String providerUrlConfigName, Class responseClass){

        Map<String, String> metaData = MapperUtils.convertValue(getAccessToken(loanMetaDataDto.getLoanVendorName()), new TypeReference<>() {});

        metaData.put(CreateLeadRequestDto.Fields.clientLoanId,loanMetaDataDto.getLoanId());
        metaData.put(LoanMetaDataDto.Fields.sanctionCode,loanMetaDataDto.getSanctionCode());

        if(loanMetaDataDto.getValidateLoanData()!=null) {
            metaData.put(CreateLeadRequestDto.Fields.panNumber, loanMetaDataDto.getValidateLoanData().getPanNumber());
            metaData.put(CreateLeadRequestDto.Fields.mobileNumber ,loanMetaDataDto.getValidateLoanData().getMobileNumber());
        }

        BaseCreditPartner partnerInfo = getPartnerInfo(loanMetaDataDto.getLoanVendorName());

        Map<String, Object> params = providerConfigUtil.getUrlAndQueryParam(partnerInfo.getId(),
                metaData,
                providerUrlConfigName);
        loanMetaDataDto.setParams(params);


        Object requestBody = null;

        Map<String, Object> template = providerConfigUtil.getProviderTemplates(loanMetaDataDto.getLoanId(),
                providerTemplateName, partnerInfo.getId(), true);

        if (template != null) {
            requestBody = jsonPathEvaluator.evaluate(template, loanMetaDataDto.getLoanId());
        }

        loanMetaDataDto.setExternalRequestBody(requestBody);

        loanMetaDataDto.setResponseClass(responseClass);


    }


    private void updateCustomInfoForCreateLead(CreateLeadRequestDto requestDto) {

        if(requestDto.getGender().equalsIgnoreCase("m")){
            requestDto.setGender("Male");
        }else if(requestDto.getGender().equalsIgnoreCase("f")){
            requestDto.setGender("Female");
        }

        if(requestDto.getLoanApplication().getLoanApplicant().getGender().equalsIgnoreCase("m")){
            requestDto.getLoanApplication().getLoanApplicant().setGender("Male");
        }else if(requestDto.getLoanApplication().getLoanApplicant().getGender().equalsIgnoreCase("f")){
            requestDto.getLoanApplication().getLoanApplicant().setGender("Female");
        }

        if(!CollectionUtils.isEmpty(requestDto.getLoanApplication().getLoanBusinessPartners())){
            requestDto.getLoanApplication().getLoanBusinessPartners().forEach(loanBusinessPartner -> {

                if(loanBusinessPartner.getGender()!=null) {
                    if (loanBusinessPartner.getGender().equalsIgnoreCase("m")) {
                        loanBusinessPartner.setGender("Male");
                    } else if (loanBusinessPartner.getGender().equalsIgnoreCase("f")) {
                        loanBusinessPartner.setGender("Female");
                    }
                }
            });
        }

    }
}
