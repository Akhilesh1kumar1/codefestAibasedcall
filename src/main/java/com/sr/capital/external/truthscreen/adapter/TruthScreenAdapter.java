package com.sr.capital.external.truthscreen.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.external.truthscreen.dto.data.*;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenEncryptedRequestDto;
import com.sr.capital.external.truthscreen.dto.response.*;
import com.sr.capital.external.truthscreen.dummyData.DummyData;
import com.sr.capital.external.truthscreen.enums.TruthScreenDocType;
import com.sr.capital.external.truthscreen.util.TruthScreenUtil;
import com.sr.capital.external.truthscreen.util.TruthScreenUtility;
import com.sr.capital.helpers.enums.ServiceName;
import com.sr.capital.util.WebClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TruthScreenAdapter {
    @Autowired
    private AppProperties appProperties;

    @Autowired
    private WebClientUtil webClientUtil;

    @Autowired
    private TruthScreenUtil truthScreenUtil;

    private final com.sr.capital.util.LoggerUtil LoggerUtil = com.sr.capital.util.LoggerUtil.getLogger(TruthScreenAdapter.class);

    public <T extends TruthScreenBaseRequest<?>, U extends TruthScreenBaseResponse<?>> U extractDetails(final T request) throws Exception {
        LoggerUtil.info("TruthScreenAdapter.extractDetails() - Start");
        String iv = TruthScreenUtility.getIV();
        String encryptedData = TruthScreenUtility.encrypt(appProperties.getAuthBridgePassword(),iv,request.getDetails());
        TruthScreenEncryptedRequestDto truthScreenEncryptedRequestDto = TruthScreenEncryptedRequestDto.builder().requestData(encryptedData).build();
        TruthScreenExternalRequestMetaData requestMetaData = getRequestEndPointAndDocType(request);

        try{
            if (appProperties.getIsTruthScreenEnable()){
                return createDummyWebHookData(requestMetaData); 
            }
             Object responseObject  = webClientUtil.makeExternalCallBlocking(ServiceName.TRUTHSCREEN,
                     appProperties.getAuthBridgeBaseUrl(),
                     requestMetaData.getEndpoint(),
                     HttpMethod.POST,
                     "",
                     truthScreenUtil.getHeaders(),
                     null,
                     truthScreenEncryptedRequestDto,
                     Object.class);
            String responseStringNeedToBeDecrypted = converter(responseObject);
            return (U) TruthScreenUtility.decrypt(
                    appProperties.getAuthBridgePassword(),
                    responseStringNeedToBeDecrypted,
                    requestMetaData.getResponseClass()
            );
        }
        catch (Exception exception){
            LoggerUtil.error(exception.getMessage());
            throw new Exception(requestMetaData.getDocType().getName() + "--" +exception.getMessage() + " ");
        }

    }

    private <T extends TruthScreenBaseRequest<?>> TruthScreenExternalRequestMetaData getRequestEndPointAndDocType(final T request) throws ServiceEndpointNotFoundException{
        if (request.getDetails() instanceof PanExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeIdSearchUrl())
                    .docType(TruthScreenDocType.PAN)
                    .responseClass(TruthScreenPanDecryptedResponseDto.class)
                    .build();
        }
        else if (request.getDetails() instanceof PanComprehensiveExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgePanComprehensiveUrl())
                    .docType(TruthScreenDocType.PAN_COMPREHENSIVE)
                    .responseClass(TruthScreenPanComprehensiveDecryptedResponse.class)
                    .build();
        }
        else if (request.getDetails() instanceof PanComplianceExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgePanComplianceUrl())
                    .docType(TruthScreenDocType.PAN_COMPLIANCE)
                    .responseClass(TruthScreenPanComplianceDecryptedResponseDto.class)
                    .build();
        } else if (request.getDetails() instanceof PanToGstExtractionRequestData) {
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgePanToGstUrl())
                    .docType(TruthScreenDocType.PAN_TO_GST)
                    .responseClass(TruthScreenPanToGstDecryptedResponseDto.class)
                    .build();
        } else if (request.getDetails() instanceof GstinExtractionRequestData) {
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeGstinUrl())
                    .docType(TruthScreenDocType.GSTIN)
                    .responseClass(TruthScreenGstinDecryptedResponseDto.class)
                    .build();
        } else if (request.getDetails() instanceof CinExtractionRequestData) {
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeCinUrl())
                    .docType(TruthScreenDocType.CIN)
                    .responseClass(TruthScreenCinDecryptResponseDto.class)
                    .build();

        } else {
            throw new ServiceEndpointNotFoundException();
        }
    }

    public String converter(Object responseObject) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse;
        if (responseObject instanceof Map) {
            jsonResponse = objectMapper.writeValueAsString(responseObject);
        } else {
            jsonResponse = responseObject.toString()
                    .replace("=", ":")
                    .replace("{", "{\"")
                    .replace("}", "\"}")
                    .replace(", ", "\", \"")
                    .replace(":", "\":\"");
        }

        Map<String, Object> responseMap = objectMapper.readValue(jsonResponse, Map.class);
        String responseDataToDecrypt = (String) responseMap.get("responseData");
        return responseDataToDecrypt;
    }

    public <U> U createDummyWebHookData(TruthScreenExternalRequestMetaData request) throws Exception {
            try {
                Object jsonResponse = null;
                if (request.getDocType() == TruthScreenDocType.GSTIN) {
                    jsonResponse = "{\n" +
                            "  \"responseData\": \"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:x1tCOhfSw+gH26b4CA3lGg==\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.PAN_TO_GST) {
                    jsonResponse = "{\n" +
                            "  \"responseData\": \"bvOeXdgFOIuPoDoaSVGqyPNms/dGX49/7k+hVpHNr6klQ9UtfQTYBXq/7RvaTxBqTOvU/B3270zfwGaaSNlY7pZwQ0kpz5tHMwZN2qpQGBoyacDlGgbPpFuP/2AQ/a20lxLWF/Gh7SGnrFosioq3D2eac8MCqcvRVP8nX1g8R78/tj0KWp+5O1rz0GtB0DJo6S5m9E/Joi0lDjzRoZWIsK4rTFxvQiul9NyJsW+K/7dahtMethQYJafp4HWrmi8kua3icVrt/Ml8lx9Hu5OLOVjt7FueCfyixQtbd1axiToBxGA3Jjd0N2T4vIVEwz4eTG6rDQqmPe79oSzb2JysfX5a2jY2gMKMuKpWKHQoAZkTV3Dthb6t64b1y+BsboWDUMEX5x0LnNibjd2iEcVjqPSIN8dh4PFpBU6+1hVQJS1esh9t1l64tg58Fr1BCvv0Pw3m1MF2NEMQgZN9GMj47Xf4N8K0Z5xpAtGGxfn2HEyoE3/NhAygQOJAnDXjhwZEO3/XGq7uGmGz4qlLeyfnp0eQI/mke1ln9XxNepcSYdyaj/U1Ty8VBzfUlAHlNAfUTF56DHa6F9r1xxMN1yhiCxbuAulA8iyt7PpGpnBaCKpT7wARIB18xtij1VTUd4Nf1MJs3AKNYPqP9AvFhcjD2Q7HLgt5URYKrUzbagdTzrLCzQAGjCiR6ynLHW8wSiNfa76ttp3/xf1T99bCt3U4YAc+e0sXvCi0x+AyBclAhaiPtE8NZjjIHKVztfxJdeH8NeoGi+DZnAQk7FwARUSIWPPeaTYxxQhs0yV8c6NznSWGulK/v4mZq0bvBIsW05hhw1PuJCuk68K5bVpRhuBV0cBn7IJ/iWRw6IH3ewQX6J5aKuOvNyWDznLTI64c6TJU4QC/PhBgOasPuy7Xb4gzsovJLIXfKoWFMnqPOW6svYquHWB2hYvlofrqyjdThMU1O1zfdcDjIpz+zYQxvnyU/oe6aLF7sNKUQvk9yI3c27FPj\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.PAN_COMPREHENSIVE) {
                    jsonResponse = "{\n" +
                            "  \"responseData\": \"OlZeFLkmaPNCec8hF5H8sD7vI9wt4qiBrpzylgZrj3nhj93N6mPoHu6nGqBcv0tiXHOJJTry/Z6809qGA+Lw2W4VgEX8L7BfbUYw3/Qumac2wiSGNaJrjM/FNSw2WHok21gqN4zIGYvYE21qyMWzweECMb/UZUR5wo5pNZ9BjtXWOyBd9che09VR8Com73Z3xxzOppzFXRZKNG0X/jlQ7FmfVp2rwL7NtryXltU6lhtr3vsmk4GxBBqsaokknPVRO7zGOHY1kYsW9nHQq/a3nHRxawoKcFh1wZ/f6PzYvgeRLnHOfExFR102h6hwxm6V+YNWZt65Zb+jKGLh2n664Pu9AuVQJm72J8QSoxB6vNe38+9SISNRH/R+7k9XER3YJnU5AY5Za1cfuRCB0+Z3s8t+jdQu49g14g2pe4fXUQX22/q6mWNl4f6q0YUu8MyTcrte2piLdwZHTsoBKA37EhAX0F52jURiZg/ZA5yG4ys1sZfCO+aM+QGmzLDhQ6sBVzXAegf68HEeNSWeON6NbVwwhZyFq1pkxy66+pgGax1nhet+1tU3MUndLhSULfT2mm0Nc49SkFD/JUsvu19zpznXcqp2aeRWRPk057ubB5bkWjgZ2W21Uko+60X8ZV9Ke4RZiXY24A+PnhmAmF0lsODWSeyUcGsuM29mDEC5mvABawbXbJG6FaU+CbVq5MwGmgIe0wBnDh3ENrQ9ByZE3z3WgGEac4QWcgDEBt+08mrMFadEQNAKCOFWzDKFYfx0A+Qlqku5mYqz3l3kStUxpshYidvglcUJBGk6QmU+F4K0I+UT5eu61HuAgBBvaxezyVA4Mxdp/1QGvzdtI7lStQ==:/341ZabwVzb6f9SW09UGig==\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.CIN) {
                    jsonResponse = "{\n" +
                    "  \"responseData\": \"c9Xjx3/imrWSVbq1K3p30qq92PDn5xHJcMT9Dm8RDUkLocRZxOIkPnNbPPlathsAXpWjJC9kXSZZc3iCVo3HmUEn4CV5MVQWQA2x9Wd2WAHHYypBa9P/TaBySjcNiR0r7BSFJz2yrP4GSJqoCKgVvg==:GzBHVRgywDcQ5oYnI5UANg==\"\n" +
                            "}";
                }
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> responseMap = objectMapper.readValue(jsonResponse.toString(), Map.class);
                String responseDataToDecrypt = (String) responseMap.get("responseData");
                return (U) TruthScreenUtility.decrypt(appProperties.getAuthBridgePassword(), responseDataToDecrypt, request.getResponseClass());
            }
            catch (Exception e){
                throw new Exception(request.getDocType().getName() + "--" + e.getMessage() + " ");
            }
    }
    
    public <U> U createDummyData(DummyData<Map<String, Object>> obj) {
        obj.setStatus("1");
        Map<String, Object> map = new HashMap<>();
        map.put("lastUpdate", "2024-12-20");
        map.put("name", "JOHN DOE");
        map.put("nameOnTheCard", "JOHN DOE");
        map.put("panHolderStatusType", "Individual");
        map.put("docNumber", "AAAAA1111A");
        map.put("status", "Inactive");
        map.put("statusDescription", "Pending Verification");
        map.put("sourceId", 2);
        obj.setMsgs(map);
        return (U) obj;
    }


}
