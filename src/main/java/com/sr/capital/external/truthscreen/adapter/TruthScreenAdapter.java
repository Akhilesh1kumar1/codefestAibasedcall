package com.sr.capital.external.truthscreen.adapter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sr.capital.config.AppProperties;
import com.sr.capital.exception.custom.ServiceEndpointNotFoundException;
import com.sr.capital.external.truthscreen.dto.data.PanExtractionRequestData;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenBaseRequest;
import com.sr.capital.external.truthscreen.dto.request.TruthScreenEncryptedRequestDto;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenBaseResponse;
import com.sr.capital.external.truthscreen.dto.response.TruthScreenDecryptedResponseDto;
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
             Object responseObject  = webClientUtil.makeExternalCallBlocking(ServiceName.TRUTHSCREEN,
                     "",
                     requestMetaData.getEndpoint(),
                     HttpMethod.POST,
                     "",
                     truthScreenUtil.getHeaders(),
                     null,
                     truthScreenEncryptedRequestDto,
                     Object.class);
//            Object jsonResponse = "{\n" +
//                    "  \"responseData\": \"uKDsHtdj50VxKiiRlLmJEsI9OA535aBDt5OczECYggLTKD6xo+wIPH/OSIda2P5jyuEKUvEPXOOD36jb/Xzt865jF2c6KoNGNWQ3jW0Z8qFJda4YoIkqfv78x25Ld4sSpcpuG3Zmp3Bnc5dBVTInAGWXHlEsbk3DvPYhjnivcCRX/mU84ce/Bpg6MStQw67y2ZgAFeCuvgUPZq17Yvp6EsK6Wc1+KZIWtz0SXz8q9U4hYz8IClGAgzEcicoPeqPbHaJAoaZhbK6hbhs7KXkaz/Osji6zYou4evdFMs62cj9uFYsihhDiOs2HRp3CxsrgT9NXu13M3PXpVcM2WHFKd/LzP28UUWxgAmS+/vWZchA=:xTjWyLnXk25F38zzL7MbGQ==\"\n" +
//                    "}";
//            ObjectMapper objectMapper = new ObjectMapper();
//            Map<String, Object> responseMap = objectMapper.readValue(jsonResponse.toString(), Map.class);
//            // Extract the responseData field from the map
//            String responseDataToDecrypt = (String) responseMap.get("responseData");
//            return (U) TruthScreenUtility.decrypt(appProperties.getAuthBridgePassword(), responseDataToDecrypt, requestMetaData.getResponseClass());
            String responseStringNeedToBeDecrypted = converter(responseObject);
            return (U) TruthScreenUtility.decrypt(
                    appProperties.getAuthBridgePassword(),
                    responseStringNeedToBeDecrypted,
                    requestMetaData.getResponseClass()
            );
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
                    .responseClass(TruthScreenDecryptedResponseDto.class)
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
