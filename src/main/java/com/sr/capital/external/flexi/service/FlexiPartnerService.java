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
import com.sr.capital.external.common.GenericCreditPartnerService;
import com.sr.capital.external.dto.response.ValidateLoanDetailsResponse;
import com.sr.capital.external.flexi.dto.response.LoanDetails;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
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
}
