package com.sr.capital.spine;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;
import com.fasterxml.jackson.core.JsonPointer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import com.sr.capital.helpers.enums.JsonPathFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *
 * EVALUATES JSON PATH EXPRESSION
 *
 * INPUT: -------
 *
 *
 * HashMap<String, String> inputTemplate = new HashMap<>();
 * inputTemplate.put("/customer/individualDetails/firstName", "$.firstName");
 * inputTemplate.put("/customer/individualDetails/lastName", "$.lastName");
 * inputTemplate.put("/customer/individualDetails/gender", "$.gender");
 * inputTemplate.put("/customer/individualDetails/dateOfBirth", "$.dateOfBirth");
 * inputTemplate.put("/customer/individualDetails/fullName", [CONCAT]->["$.firstName", " ",
 * "$.lastName"]); inputTemplate.put("/customer/individualDetails/isMale",
 * [IF]->["'${genderData}'.equals('MALE')", "YES", "NO", {"genderData"->"$.gender"}]);
 *[SWITCH]->[{"'${genderData}'.equals('MALE')":"male","'${genderData}'.equals('FEMALE')":"female"},{"genderData"->"$.gender"}]
 * Data that will be placed in template: seedData['firstName'] = "Adam" seedData['lastName'] = "Eve"
 *
 *
 * OUTPUT: ------- { "customer" : { "individualDetails" : { "firstName" : "fn", "dateOfBirth" :
 * "22-02-1997", "gender" : "MALE", "lastName" : "sn" } }
 *
 */

@Slf4j
@Component
public class JsonPathEvaluator implements IPathEvaluator {

    private final String VARIABLE_DECLARATION = "$.";
    private ObjectMapper mapper;
    private SpelEvalualator spelEvalualator;

    @Autowired
    public JsonPathEvaluator(ObjectMapper mapper, SpelEvalualator spelEvalualator) {
        this.mapper = mapper;
        this.spelEvalualator = spelEvalualator;
    }

    /**
     * @param template : Object OR HashMap[where key is the output path and value is the jsonPath
     *        expression] OR it can be plain String in case you want to parse a single value
     * @param seedData : Data which we need to feed into the template
     * @return
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public Object evaluate(Object template, Object seedData) {
        if (template instanceof Map) {
            return this.evaluateMap((HashMap<String, Object>) template, seedData);
        }

        return evaluateValue(template, seedData);
    }

    /**
     * @param template : Object OR HashMap[where key is the output path and value is the jsonPath
     *        expression] OR it can be plain String in case you want to parse a single value
     * @param seedData : Data which we need to feed into the template
     * @param valueType: Class you want to cast the evaluated object
     * @param <T>
     * @return
     */
    @Override
    public <T> T evaluate(Object template, Object seedData, Class<T> valueType) {
        Object objectNode = this.evaluate(template, seedData);
        String objectNodeToString = objectNode.toString();

        try {
            return mapper.readValue(objectNodeToString, valueType);
        } catch (JsonProcessingException e) {
            log.error("error while casting: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Forms parent child relationship
     *
     * @param node: Final Object
     * @param pointer: Path
     * @param value: Values that needs to be added
     */
    private void setJsonPointerValue(ObjectNode node, JsonPointer pointer, JsonNode value) {
        JsonPointer parentPointer = pointer.head();
        JsonNode parentNode = node.at(parentPointer);
        String fieldName = pointer.last().toString().substring(1);

        if (parentNode.isMissingNode() || parentNode.isNull()) {
            parentNode =
                    StringUtils.isNumeric(fieldName) ? mapper.createArrayNode() : mapper.createObjectNode();
            setJsonPointerValue(node, parentPointer, parentNode); // recursively reconstruct hierarchy
        }

        if (parentNode.isArray()) {
            ArrayNode arrayNode = (ArrayNode) parentNode;
            int index = Integer.valueOf(fieldName);
            // expand array in case index is greater than array size (like JavaScript does)
            for (int i = arrayNode.size(); i <= index; i++) {
                arrayNode.addNull();
            }
            arrayNode.set(index, value);
        } else if (parentNode.isObject()) {
            ((ObjectNode) parentNode).set(fieldName, value);
        } else {
            throw new IllegalArgumentException(
                    "`" + fieldName + "` can't be set for parent node `" + parentPointer
                            + "` because parent is not a container but " + parentNode.getNodeType().name());
        }
    }

    /**
     * Takes data in the Map format where key is the path and value is the jsonPath expression
     *
     * @param inputTemplate
     * @param seedData
     * @return
     */
    private Object evaluateMap(HashMap<String, Object> inputTemplate, Object seedData) {
        if (ObjectUtils.isEmpty(inputTemplate)) {
            return seedData;
        }

        try {
            ObjectNode recreationRequest = mapper.createObjectNode();

            DocumentContext context = JsonPath.parse(mapper.writeValueAsString(seedData));
            for (Map.Entry<String, Object> entry : inputTemplate.entrySet()) {
                String pathToSet = entry.getKey();

                JsonNode finalPath = null;
                Object pathForData = entry.getValue();
                if (pathForData instanceof Map) {
                    finalPath = extractValueByEvaluatingFunction(context, pathForData);
                } else {
                    String path = entry.getValue().toString();
                    finalPath = extractValue(context, path);
                }

                if (finalPath == null) {
                    continue;
                }

                /* Create parent-child hierarchy */
                setJsonPointerValue(recreationRequest, JsonPointer.compile(pathToSet), finalPath);
            }


            log.debug("====Json Path eval = {}",
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(recreationRequest));
            return recreationRequest;
        } catch (JsonProcessingException ex) {
            return seedData;
        }
    }

    /**
     * Evaluates a path
     *
     * @param path
     * @param seedData
     * @return
     */
    private Object evaluateValue(Object path, Object seedData) {

        Object evaluatedValue = null;
        try {
            DocumentContext context = JsonPath.parse(mapper.writeValueAsString(seedData));
            try {
                String text_value = context.read(path.toString()).toString();
                evaluatedValue = mapper.readTree(text_value);
            } catch (JsonProcessingException e) {
                evaluatedValue = new TextNode(context.read(path.toString()).toString());
            } catch (PathNotFoundException ex) {

            }
        } catch (JsonProcessingException e) {
            return null;
        }

        return evaluatedValue;
    }


    private JsonNode extractValue(DocumentContext context, String path) {
        JsonNode finalPath = null;
        /* Constant value */
        if (path != null && (path.length() < 2 || !VARIABLE_DECLARATION.equals(path.substring(0, 2)))) {
            try {
                Object obj = mapper.readValue(path, Object.class);
                finalPath = mapper.valueToTree(obj);
            } catch (Exception ex) {
                /* if path is a constant String/Primitive type */
                finalPath = new TextNode(path);
            }
        } else {
            try {
                /* Works for Json, Array, Pojo's */
                String text_value = mapper.writeValueAsString(context.read(path));
                finalPath = mapper.readTree(text_value);
            } catch (JsonProcessingException e) {
                /* When value is primitive */
                finalPath = new TextNode(context.read(path).toString());
            } catch (Exception ex) {
                /* case when seedData doesn't have value for the required path */
                finalPath = null;
            }
        }

        return finalPath;
    }


    private JsonNode extractValueByEvaluatingFunction(DocumentContext context, Object pathForData) {
        Map<String, List> functionalOperation = (Map<String, List>) pathForData;
        String functionName = functionalOperation.keySet().iterator().next();

        JsonNode evaluatedValue = null;
        switch (functionName) {
            case "IF":
                evaluatedValue = performIfOperation(context, functionalOperation);
                break;

            case "CONCAT":
                evaluatedValue = performConcatFunction(context, functionalOperation);
                break;
            case "SPEL":
                evaluatedValue = performSpelFunction(context, functionalOperation);
                break;
            case "SWITCH":
                evaluatedValue = performSwitchFunction(context, functionalOperation);
                break;
        }

        return evaluatedValue;
    }



    @SuppressWarnings({"rawtypes", "unchecked"})
    private JsonNode performSpelFunction(DocumentContext context,
                                         Map<String, List> functionalOperation) {
        JsonNode finalPath = null;

        try {
            List<Object> paths = functionalOperation.get(JsonPathFunction.SPEL.name());

            Map<String, Object> spelParams = (Map<String, Object>) paths.get(1);
            Map<String, Object> parsedSpelParams = new HashMap<>();
            for (String key : spelParams.keySet()) {
                parsedSpelParams.put(key, extractTextValueForComplexPath(context, spelParams.get(key)));
            }

            Object evalValue = spelEvalualator.evaluate(paths.get(0), parsedSpelParams);
            try {
                finalPath = mapper.valueToTree(evalValue);
            } catch (Exception ex) {
                /* if path is a constant String/Primitive type */
                finalPath = new TextNode(evalValue.toString());
            }


        } catch (Exception ex) {
            log.error("something went wrong {}", ex.getMessage());
        }

        return finalPath;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JsonNode performConcatFunction(DocumentContext context,
                                           Map<String, List> functionalOperation) {
        StringBuilder concatenatedValues = new StringBuilder();
        List<Object> paths = functionalOperation.get(JsonPathFunction.CONCAT.name());
        for (Object path : paths) {
            concatenatedValues.append(extractTextValueForComplexPath(context, path));
        }

        return new TextNode(concatenatedValues.toString());
    }


    private JsonNode performSwitchFunction(DocumentContext context, Map<String, List> functionalOperation) {
        JsonNode finalPath = null;
        List<Object> paths = functionalOperation.get(JsonPathFunction.SWITCH.name());
        Map<String, Object> spelParams = (Map<String, Object>) paths.get(1);
        Map<String, Object> parsedSpelParams = new HashMap<>();

        for (String key : spelParams.keySet()) {
            parsedSpelParams.put(key, extractTextValueForComplexPath(context, spelParams.get(key)));
        }
        Map<String,Object> multiconditions = (Map<String, Object>) paths.get(0);
        for(String keySetCondition : multiconditions.keySet()){
            if(Boolean.valueOf(spelEvalualator.evaluate(keySetCondition, parsedSpelParams).toString()))
            {
                return extractValue(context, multiconditions.get(keySetCondition).toString());
            }

        }
        return finalPath;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private JsonNode performIfOperation(DocumentContext context,
                                        Map<String, List> functionalOperation) {
        try {

            List<Object> paths = functionalOperation.get(JsonPathFunction.IF.name());

            Map<String, Object> spelParams = (Map<String, Object>) paths.get(3);
            Map<String, Object> parsedSpelParams = new HashMap<>();
            for (String key : spelParams.keySet()) {
                parsedSpelParams.put(key, extractTextValueForComplexPath(context, spelParams.get(key)));
            }

            if (Boolean.valueOf(spelEvalualator.evaluate(paths.get(0), parsedSpelParams).toString())) {
                return extractValue(context, paths.get(1).toString());
            } else {
                return extractValue(context, paths.get(2).toString());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return null;
    }

    private String extractTextValueForComplexPath(DocumentContext context, Object path) {
        try {
            String pathValue = context.read(path.toString()).toString();
            if (pathValue == null) {
                return "";
            }

            return pathValue;
        } catch (Exception e) {
            if (path != null && (path.toString().length() < 2
                    || !VARIABLE_DECLARATION.equals(path.toString().substring(0, 2)))) {

                /* In case of constant value */
                return path.toString();
            }

            return StringUtils.EMPTY;
        }
    }
}
