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
                            "  \"responseData\": \"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:ygoD5XTsd8tO3j79hjLQWw==\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.PAN_TO_GST) {
                    jsonResponse = "{\n" +
                            "  \"responseData\": \"PRbeAvKre8y7UpkKFn7nL6SYUCimD6eN9K3S71oRipsrM/ZJmR83rRyAb6qktAaTlSTFH0zrLFUt+EsfT3e/ZV/WAgtf4u05oPcTvjzZDiY2RCD1AHLOTBlLtDTdm2Ss0EZQL3ji+Q+38To7/zwjqxG2s6ALGb822YNcRthfvIw=:DetwG47WGfLzA18/I9r82A==\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.PAN_COMPREHENSIVE) {
                    jsonResponse = "{\n" +
                            "  \"responseData\": \"T/viOFoPCoclixQew/UEJpUZHaR2jGBP2wrYZXTOiokT1V2nrv+xTHLEeZVXSF9um+heO379riuCPlyOV27WjlWb7yv4OHbGROn0OsaM6eIUm1IT8cy2fMZJxHpbvJGxI2zXXTLCrVcR6IPCwXm5WSl/s1UsA4rsLally18c2IunPz7Q3xaD6IrGNm0RsOJcWMd8HLvqCNyd8aagmSfUTkXV2CdJrKH2sC8xx/Al59kl70u2ZJiPXHNmqu8ZxDsFofK+KegtlKLEfJcOB7v30uO92+nswvIGKC5VgQGeWWxJ16pD8FeytzxJCSw3YO7P1xi2enkOKRXASh6f/tdZeFBcjE1UkmjN56EPDSPU6FMF0443S+0qm+ymTaNJ8Cqm+gcHjx9lcWJWLvBEDL4xYo0Lvb201+j2dihuv5CjeqByCIRsyMpWch1v7pyz2bSuomx6JKRQnV3+oHr2N0yHaYPgUyfL7fXeG25BHcpwBOoYDJl2TWvdn5BIrDX3xVxDU8+LSmSxMXQJblkGSEPOcTtrxyhEGs/+LZkos8RAk98Vj1z07v9BSsC6V/9KxyYd+CwYr54wS8DeMIN2quxLqjA0WNg0iIe4RoFi3+BP+WXJXS6ECoocQm/u9g/m/ipJM6v9G1CaDdyHSkkGo7zg5ErXahR1SNGhi5srV50mXQPlj4HPB58O6QvmnyOET3x7YdicZy5QJOZGIbsDC1ZTX/vUuCNeMe9AkoZW4vfC0yDpwU91KvkGOU/sjogy15UMVrXPGqrXgp8dY5M91SDp2ruEJpKudQf2eivx3tJByuM=:2W1lHZ9Co8QD/jzZY7469Q==\"\n" +
                            "}";
                } else if (request.getDocType() == TruthScreenDocType.CIN) {
                    jsonResponse = "{\n" +
                    "  \"responseData\": \"AlDNR1Gp2sjnPHB0ozaGm4RZoWT9p0lBFTM7Vo7q/UPCgXT9YoEeWgaNqMtKWB80MX+26nZ/32rH334q0GuiSoTP0hIJbD3A9WqgaSl3sU2eTf/vSaInAPpUZmgARgupstp1VS+lPK2HMcAuzWs4CA==:FMvI6maXWyDGE8B/bvDf2g==\"\n" +
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
