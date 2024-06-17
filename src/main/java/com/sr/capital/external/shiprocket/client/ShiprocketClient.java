package com.sr.capital.external.shiprocket.client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.CustomException;
import com.sr.capital.external.dto.request.ValidateTokenRequest;
import com.sr.capital.external.dto.response.ValidateTokenResponse;
import com.sr.capital.external.shiprocket.dto.response.ApiTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.GetCompanyDetails;
import com.sr.capital.external.shiprocket.dto.response.InternalTokenUserDetailsResponse;
import com.sr.capital.external.shiprocket.dto.response.KycResponse;
import com.sr.capital.util.LoggerUtil;
import com.sr.capital.util.WebClientUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static com.omunify.restutil.RestUtilProvider.getInstance;
import static com.sr.capital.helpers.enums.ServiceName.SHIPROCKET;
import static org.apache.http.HttpStatus.SC_OK;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShiprocketClient {

    LoggerUtil loggerUtil = LoggerUtil.getLogger(ShiprocketClient.class);

    final AppProperties appProperties;

    final WebClientUtil webClientUtil;

    public boolean validateToken(String token) throws UnirestException, CustomException {

        Map<String, String> headers = getHeaders(token);
        String url = appProperties.getShiprocketAuthBaseUrl() + appProperties.getShiprocketValidateTokenEndPoint();

        ValidateTokenRequest request = ValidateTokenRequest.builder().token(token).validateTokenRequest(1).build();

        HttpResponse<ValidateTokenResponse> validateTokenResponse = getInstance().withHeaders(headers).post(url,
                request, ValidateTokenResponse.class);

        if ((validateTokenResponse.getStatus() != SC_OK && validateTokenResponse.getBody() == null)
                || !validateTokenResponse.getBody().getCode()
                        .equalsIgnoreCase(String.valueOf(org.apache.http.HttpStatus.SC_OK))) {
            loggerUtil.error("Received response: " + validateTokenResponse.getBody() + " with status code: "
                    + validateTokenResponse.getStatus() + " while validating token");
            throw new CustomException(
                    String.format("Received response: %s with status code: %s while validating token ",
                            validateTokenResponse.getBody(),
                            validateTokenResponse.getStatus()),
                    HttpStatus.UNAUTHORIZED);
        }

        return true;
    }

    private Map<String, String> getHeaders(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }

    public KycResponse getKycDetails(String token) throws UnirestException, CustomException {

        Map<String, String> headers = getHeaders(token);
        String url = appProperties.getShiprocketApiBaseUrl() + appProperties.getShiprocketKycEndPoint();

        HttpResponse<KycResponse> kycResponse = getInstance().withHeaders(headers).get(url,
                KycResponse.class);

        if (kycResponse.getStatus() != SC_OK && kycResponse.getBody() == null) {
            loggerUtil.error("Received response: " + kycResponse.getBody() + " with status code: "
                    + kycResponse.getStatus() + " while fetching KYC details");
            throw new CustomException(
                    String.format("Received response: %s with status code: %s while fetching KYC details ",
                            kycResponse.getBody(),
                            kycResponse.getStatus()),
                    HttpStatus.BAD_REQUEST);
        }

        return kycResponse.getBody();
    }


    public ApiTokenUserDetailsResponse getUserDetailsUsingApiToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        return webClientUtil.makeExternalCallBlocking(SHIPROCKET, appProperties.getShiprocketApiBaseUrl(),
                appProperties.getShiprocketApiTokenUserDetailsEndpoint(), HttpMethod.GET,"test",
                headers, null, null, ApiTokenUserDetailsResponse.class);
    }

    public InternalTokenUserDetailsResponse getUserDetailsUsingInternalToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        HashMap<String, String> params = new HashMap<>();
        params.put("is_web", "1");
        return webClientUtil.makeExternalCallBlocking(SHIPROCKET, appProperties.getShiprocketApiBaseUrl(),
                appProperties.getShiprocketInternalTokenUserDetailsEndpoint(), HttpMethod.GET, "test",
                headers, params, null, InternalTokenUserDetailsResponse.class);
    }


    public GetCompanyDetails getCompanyDetails(String token) throws UnirestException, CustomException {

        Map<String, String> headers = getHeaders(token);
        String url = appProperties.getShiprocketApiBaseUrl() + appProperties.getShiprocketValidateTokenEndPoint();


        HttpResponse<GetCompanyDetails> validateTokenResponse = getInstance().withHeaders(headers).get(url, GetCompanyDetails.class);

     /*   if ((validateTokenResponse.getStatus() != SC_OK && validateTokenResponse.getBody() == null)
                || !validateTokenResponse.getBody().getCode()
                .equalsIgnoreCase(String.valueOf(org.apache.http.HttpStatus.SC_OK))) {
            loggerUtil.error("Received response: " + validateTokenResponse.getBody() + " with status code: "
                    + validateTokenResponse.getStatus() + " while validating token");
            throw new CustomException(
                    String.format("Received response: %s with status code: %s while validating token ",
                            validateTokenResponse.getBody(),
                            validateTokenResponse.getStatus()),
                    HttpStatus.UNAUTHORIZED);
        }*/

        return validateTokenResponse.getBody();
    }
}
