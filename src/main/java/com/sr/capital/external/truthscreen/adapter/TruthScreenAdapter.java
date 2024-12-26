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
                    "  \"responseData\": \"hPK6K8eW0LMNnBr99v7+a3p0kKGesxxqjaFNlFosmtPwTWWqiAqlZGK5QoWuhOOOnlxLDA12Keu5xSpYs3mXG0Faztt2vl4FkwFI++rdaXp1ZxX+GkHkjo+lkspN0HzBb+LiwMwlfJDVhnOWCekhOCPTIWpiS7Skln4QywFy6s9v5WXbA5M62w6pUQTTrmc5rd/MPvfe/Lvjx/RbbW3ij8Q0BlePjMs2u8YqI8YG27oeQP77n3Mby64K+JIlNdHxq1lXkpspEE44OB85Zz8hPC48aA99KeCkreF+PT7AMQHzWm1e1UA3JvV3RLmGrt6OSUfRrxiOyZCm5B/fk2diUeb65w4rHD7pZQ2W3nm8Fjg89P0qvDVDl4vyZykd/VWIeiHNWuwi6n1m6r1HpH6HHHVN6c59/G/YAVlt3x5hphE=:zmRF/jauzgJp75cdeu56Hg==\"\n" +
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
                    .responseClass(TruthScreenPanDecryptedResponseDto.class)
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
