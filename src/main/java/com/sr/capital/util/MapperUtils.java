package com.sr.capital.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Slf4j
public class MapperUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ModelMapper modelMapper = new ModelMapper();

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <S, T> T mapClass(S source, Class<T> targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        return modelMapper.map(source, targetClass);
    }
    public static <S, T> T mapClass(S source, T targetClass) {
        modelMapper.getConfiguration().setMatchingStrategy(STRICT);
        modelMapper.map(source, targetClass);
        return targetClass;
    }

    public static String writeValueAsString(Object object, boolean pretty) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
        return (pretty) ? objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object) : objectMapper.writeValueAsString(object);
    }

    public static <T> T readValue(String object, Class<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.readValue(object, objectClass);
    }

    public static <T> T readValue(String object, TypeReference<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.readValue(object, objectClass);
    }

    public static <V, T> T convertValue(V object, Class<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.convertValue(object, objectClass);
    }

    public static <V, T> T convertValue(V object, Class<T> objectClass, Module module) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(module);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.convertValue(object, objectClass);
    }

    public static <T> T readValueForParameterizedType(String object, TypeReference<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper.readValue(object, objectClass);
    }

    public static <T> T convertValue(Object fromValue, TypeReference<T> toValueTypeRef) {
        return new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false).convertValue(fromValue, toValueTypeRef);
    }

    public static String writeValueAsString(Object object) throws JsonProcessingException {
        return new ObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false).writeValueAsString(object);
    }

    public static <T> T getBean(Map<String, Object> value, Class<T> objectClass) throws IOException {
        ObjectMapper  objectMapper = new ObjectMapper();
        String valueString = objectMapper.writeValueAsString(value);
        return objectMapper.readValue(valueString,objectClass);
    }


    public static String writeValueAsStringWithoutExcepton(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            String errMessage = String.format("JsonProcessingException while converting Object::{} to String ", object);
            log.error(errMessage);
            throw new RuntimeException(errMessage);
        }
    }

    public static String writeValueAsStringWithWrapRootEnabled(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
        return objectMapper.writeValueAsString(object);
    }

    public static <T> T readValueAsTree(JsonNode details, Class<T> objectClass) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return objectMapper.treeToValue(details, objectClass);
    }

    public static <T> T readValueWithDeserializationOnUnknown(String orderDetails, Class<T> objectClass, boolean failOnUnknownProperties) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
        return objectMapper.readValue(orderDetails, objectClass);
    }

    public static String convertToString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {

        }
        return null;
    }

    public static JsonNode convertToJsonNode(String obj){
        try {
            return objectMapper.readTree(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
