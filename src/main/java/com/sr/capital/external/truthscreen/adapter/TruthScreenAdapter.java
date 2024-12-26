package com.sr.capital.external.truthscreen.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.external.truthscreen.dto.data.PanComplianceExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.data.PanComprehensiveExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenEncryptedRequestDto;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenPanComplianceDecryptedResponseDto;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenPanComprehensiveDecryptedResponse;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenPanDecryptedResponseDto;
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

        String iv = TruthScreenUtility.getIV();
        String encryptedData = TruthScreenUtility.encrypt(appProperties.getAuthBridgePassword(),iv,request.getDetails());
        TruthScreenEncryptedRequestDto truthScreenEncryptedRequestDto = TruthScreenEncryptedRequestDto.builder().requestData(encryptedData).build();
        TruthScreenExternalRequestMetaData requestMetaData = getRequestEndPointAndDocType(request);

        try{
//             Object responseObject  = webClientUtil.makeExternalCallBlocking(ServiceName.TRUTHSCREEN,
//                     "",
//                     requestMetaData.getEndpoint(),
//                     HttpMethod.POST,
//                     "",
//                     truthScreenUtil.getHeaders(),
//                     null,
//                     truthScreenEncryptedRequestDto,
//                     Object.class);
            Object jsonResponse = "{\n" +
                    "  \"responseData\": \"gTv4NV941WkN9qpCZPYueofW9zCmNyqwpMeuoGWGuoJHKkKF8tgXZOjnLWgdwVmgXZ1e/XEmv0j9rZct/elU+0Qd7XZaNTqK3sCSm8fUokjbYA1LLA0DxkIxcguGoc4Lw3TRItemflXozABpQNd6UjvbNXEf74loEAdhjWg7DXnqA690c2UvGjHZlBI98zLI7/tANHxHlxKKI9E3BE+oZUvJUDeY9Ahr7EDUEZ7Cf4EIH9s3pJJXZ9ZepVurvWuZw4ewMGCciQeXSiLqtErrCw8FnxnM1OD45BCLedET+WRw9T9RPwBo3TnaBUR092xiWLXoeLwSA3Ml7iJfmli7gypcKHY8Q+y99eb4fuwJAwmRxUA18riPwdRayoTfl+K++I+wzLuAKhbejJDJLBVz/2Kr7G7ANO2kRcMrK+Y1TY6LFpLn8SlP931gHf4ryc8sR0jujI05NMyMla6d8P4QPYo4+tYklQCg0LU15+Chy3NvDgdHlN/a9gNSANOJBMAgqNT7Q+clfuWhmixAJoyQLlScOhtBupuMM+fb+F+ZZQfcQv4mq5oX4NdoNHMvwCIwCIwvDn29pbjWnbwyMNRoQ9MuBN4R+IKXrTSoU2SnFR58gLrto5a+LN2k5S0H8mZsls9ASY+u9gE8xHuVIn8EmwCwtYBVRjx89yiSdUaw6xwc+ZW/ZWvq8QGkK99WI8QZ9SBBAW0skiScH/hlU6IFKBoQGZj/hPXDla918CXGsAYfqGXhIdEMo+J3PBSleaXSo5mdBV6ERMgy7NTVdZx6wWw5tbUUI5Y1bxDkHUZZdY0=:uZuk8meOzU5BZCxAyWuerg==\"\n" +
                    "}";
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse.toString(), Map.class);
            // Extract the responseData field from the map
            String responseDataToDecrypt = (String) responseMap.get("responseData");
            return (U) TruthScreenUtility.decrypt(appProperties.getAuthBridgePassword(), responseDataToDecrypt, requestMetaData.getResponseClass());
//            String responseStringNeedToBeDecrypted = converter(responseObject);
//            return (U) TruthScreenUtility.decrypt(
//                    appProperties.getAuthBridgePassword(),
//                    responseStringNeedToBeDecrypted,
//                    requestMetaData.getResponseClass()
//            );
        }
        catch (Exception exception){
            LoggerUtil.error(exception.getMessage());
            throw new Exception(requestMetaData.getDocType().getName() + "--" +exception.getMessage());
        }

    }

    private <T extends TruthScreenBaseRequest<?>> TruthScreenExternalRequestMetaData getRequestEndPointAndDocType(final T request) throws ServiceEndpointNotFoundException{
        if (request.getDetails() instanceof PanExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeBaseUrl()+appProperties.getAuthBridgeIdSearchUrl())
                    .docType(TruthScreenDocType.PAN)
                    .responseClass(TruthScreenPanDecryptedResponseDto.class)
                    .build();
        }
        else if (request.getDetails() instanceof PanComprehensiveExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeBaseUrl()+appProperties.getAuthBridgePanComprehensiveUrl())
                    .docType(TruthScreenDocType.PAN_COMPREHENSIVE)
                    .responseClass(TruthScreenPanComprehensiveDecryptedResponse.class)
                    .build();
        }
        else if (request.getDetails() instanceof PanComplianceExtractionRequestData){
            return TruthScreenExternalRequestMetaData.builder()
                    .endpoint(appProperties.getAuthBridgeBaseUrl()+appProperties.getAuthBridgePanComplianceUrl())
                    .docType(TruthScreenDocType.PAN_COMPLIANCE)
                    .responseClass(TruthScreenPanComplianceDecryptedResponseDto.class)
                    .build();
        }
        else {
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
