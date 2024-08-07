package com.sr.capital.external.common;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.omunify.core.model.GenericResponse;
import com.sr.capital.dto.request.AccessTokenRequestDto;
import com.sr.capital.dto.response.AccessTokenResponseDto;
import com.sr.capital.entity.mongo.CreditPartnerConfig;
import com.sr.capital.entity.primary.BaseCreditPartner;
import com.sr.capital.exception.custom.InvalidVendorCodeException;
import com.sr.capital.exception.custom.InvalidVendorTokenException;
import com.sr.capital.helpers.constants.Constants;
import com.sr.capital.helpers.enums.ProviderRequestTemplateType;
import com.sr.capital.helpers.enums.ProviderResponseTemplateType;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.repository.mongo.CreditPartnerConfigRepository;
import com.sr.capital.repository.primary.BaseCreditPartnerRepository;
import com.sr.capital.service.CreditPartnerService;
import com.sr.capital.spine.JsonPathEvaluator;
import com.sr.capital.util.MapperUtils;
import com.sr.capital.util.ProviderConfigUtil;
import com.sr.capital.util.ProviderHelperUtil;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GenericCreditPartnerService implements CreditPartnerService {

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

    @Override
    public AccessTokenResponseDto getAccessToken(String partner) {
        RMapCache<String, AccessTokenResponseDto> accessTokenInfo = redissonClient
                .getMapCache(Constants.RedisKeys.ACCESS_TOKEN);

        AccessTokenResponseDto responseDto = accessTokenInfo.get(partner);

        if (responseDto == null) {
            BaseCreditPartner partnerInfo = getPartnerInfo(partner);

            CreditPartnerConfig partnerConfig = creditPartnerConfigRepository
                    .findByPartnerId(partnerInfo.getId());

            AccessTokenRequestDto requestDto = MapperUtils.mapClass(partnerConfig, AccessTokenRequestDto.class);
            requestDto.setPartner(partner);

            Map<String, Object> params = providerConfigUtil.getUrlAndQueryParam(partnerInfo.getId(),
                    requestDto.getMetaData(),
                    ProviderRequestTemplateType.GET_TOKEN.name());

            Object requestBody = null;

            Map<String, Object> template = providerConfigUtil.getProviderTemplates(requestDto,
                    ProviderRequestTemplateType.GET_TOKEN.name(), partnerInfo.getId(), true);

            if (template != null) {
                requestBody = jsonPathEvaluator.evaluate(template, requestDto);
            }

            HttpResponse<?> restResponseEntity = null;
            try {
                restResponseEntity = providerHelperUtil.makeApiCall(params,
                        (String) params.getOrDefault(ProviderUrlConfigTypes.BASE_URL.name(), ""),
                        requestBody,
                        null);
            } catch (UnirestException | URISyntaxException e) {
                log.error(partner, e);
            }

            GenericResponse<?> response = new GenericResponse<>();

            providerHelperUtil.setResponse(response, restResponseEntity,
                    ProviderResponseTemplateType.GET_TOKEN_RESPONSE.name(), partnerInfo.getId());

            try {
                responseDto = MapperUtils.convertValue(response.getData(),
                        AccessTokenResponseDto.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            accessTokenInfo.put(partner, responseDto, 10, TimeUnit.MINUTES);
        }
        return responseDto;
    }

    @Override
    public Boolean validateExternalRequest(String vendorToken, String vendorCode)
            throws InvalidVendorCodeException, InvalidVendorTokenException {
        return null;
    }

    private BaseCreditPartner getPartnerInfo(String partner) {
        RMapCache<String, BaseCreditPartner> partnersInfo = redissonClient
                .getMapCache(Constants.RedisKeys.BASE_CREDIT_PARTNER);
        BaseCreditPartner partnerInfo = partnersInfo.get(partner);

        if (partnerInfo == null) {
            partnerInfo = baseCreditPartnerRepository.findByCreditPartnerName(partner);
        }

        return partnerInfo;
    }
}
