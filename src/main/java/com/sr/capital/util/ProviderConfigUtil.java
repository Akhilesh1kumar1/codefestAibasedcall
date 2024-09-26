package com.sr.capital.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.sr.capital.entity.primary.ProviderTemplateConfigEntity;
import com.sr.capital.entity.primary.ProviderUrlConfigEntity;
import com.sr.capital.helpers.enums.ProviderUrlConfigTypes;
import com.sr.capital.service.entityimpl.ProviderTemplateConfigServiceImpl;
import com.sr.capital.service.entityimpl.ProviderUrlConfigEntityServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.sr.capital.helpers.constants.ProviderServiceConstants.DYNAMIC;
import static com.sr.capital.helpers.constants.ProviderServiceConstants.STATIC_TYPE;

@Service
@AllArgsConstructor
@Slf4j
public class ProviderConfigUtil {

    final ProviderTemplateConfigServiceImpl providerTemplateConfigService;

    final ProviderUrlConfigEntityServiceImpl providerUrlConfigEntityService;

    public Map<String, Object> getUrlAndQueryParam(Long partnerId, Map<String, String> metaData, String group) {
        Map<String, Object> params = new HashMap<>();
        Map<String, String> queryParams = new HashMap<>();
        Map<String, String> headerParam = new HashMap<>();
        Map<String, String> pathVariables = new HashMap<>();
        System.out.println("[getUrlAndQueryParam] start channelId:" + params);

        List<ProviderUrlConfigEntity> urlConfig = providerUrlConfigEntityService.getUrlConfig(partnerId, group, true);
        if (urlConfig != null && !urlConfig.isEmpty()) {
            for (ProviderUrlConfigEntity providerConfig : urlConfig) {
                switch (providerConfig.getPartnerConfigTypes()) {
                    case "BASE_URL":
                        params.put(ProviderUrlConfigTypes.BASE_URL.name(), providerConfig.getValue());
                        break;
                    case "QUERY_PARAM":
                        queryParams.put(providerConfig.getKey(),
                                metaData.getOrDefault(providerConfig.getValue(), providerConfig.getValue()));
                        break;
                    case "HEADER":
                        headerParam.put(providerConfig.getKey(),
                                metaData.getOrDefault(providerConfig.getValue(), providerConfig.getValue()));
                        break;
                    case "PATH_VARIABLE":
                        pathVariables.put(providerConfig.getKey(),
                                metaData.getOrDefault(providerConfig.getValue(), providerConfig.getValue()) );
                        break;
                    case "OTHER":
                        params.put(providerConfig.getKey(), providerConfig.getValue());
                        break;
                }
            }
            params.put(ProviderUrlConfigTypes.QUERY_PARAM.name(), queryParams);
            params.put(ProviderUrlConfigTypes.HEADER.name(), headerParam);
            params.put(ProviderUrlConfigTypes.PATH_VARIABLE.name(),pathVariables);
        } else {
            log.error("[getUrlAndQueryParam] no url info found for partnerId %d group %s%n", partnerId, group);
        }
        log.info("[getUrlAndQueryParam] End Params:" + params);

        return params;
    }

    public String replaceParam(Map<String, String> vals, String pattern) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> val : vals.entrySet()) {
            result = new StringBuilder(pattern.replace("{" + val.getKey() + "}", val.getValue()));
            pattern = result.toString();
        }
        return result.toString();
    }

    public Map<String, Object> getProviderTemplates(Object object, String group, Long partnerId, Boolean enabled) {
        Map<String, Object> templates = new HashMap<>();
        log.info("[getproviderTemplates] start :");

        List<ProviderTemplateConfigEntity> providerTemplateList =
                providerTemplateConfigService.getProviderTemplate(group, STATIC_TYPE, partnerId, enabled);
        if (providerTemplateList != null) {
            for (ProviderTemplateConfigEntity template : providerTemplateList) {
                templates.put(template.getDestination(), template.getSource());
            }
        }
        providerTemplateList = providerTemplateConfigService.getProviderTemplate(group, DYNAMIC, partnerId, enabled);

        if (providerTemplateList != null && !providerTemplateList.isEmpty()) {
            JsonNode rootNode = MapperUtils.convertToJsonNode(MapperUtils.convertToString(object));
            for (ProviderTemplateConfigEntity providerTemplate : providerTemplateList) {
                String[] sourceSplitArray = providerTemplate.getSource().split("\\.");
                JsonNode childNode = rootNode;
                Set<String> sourceSet = new LinkedHashSet<>();
                StringBuilder sourcePrefix = new StringBuilder("$.");
                int i = 1;
                while (i < sourceSplitArray.length && childNode != null) {
                    if (sourceSplitArray[i].contains("[0]")) {
                        String nodeKey = sourceSplitArray[i].substring(0, sourceSplitArray[i].indexOf("["));
                        childNode = childNode.get(nodeKey);
                        if (childNode != null && childNode.size() > 0) {
                            if (i + 1 < sourceSplitArray.length && sourceSplitArray[i + 1].contains("[0]")) {
                                int k = 0;
                                JsonNode childNode1 = childNode;
                                while (k < childNode1.size()) {
                                    nodeKey = sourceSplitArray[i + 1].substring(0, sourceSplitArray[i + 1].indexOf("["));
                                    childNode = childNode1.get(k).get(nodeKey);
                                    String source = sourcePrefix + sourceSplitArray[i].replace("0", "" + k);
                                    StringBuilder destination = null;
                                    int oneIndex = 0;
                                    if (providerTemplate.getDestination().contains("*")) {
                                        oneIndex = providerTemplate.getDestination().indexOf("*");
                                        destination = new StringBuilder(providerTemplate.getDestination().substring(0, oneIndex) + k);
                                    }
                                    if (childNode != null && childNode.size() > 0) {
                                        int j = 0;
                                        while (j < childNode.size()) {
                                            int index = providerTemplate.getSource().indexOf(sourceSplitArray[i + 1]) + sourceSplitArray[i + 1].length();
                                            String source1 = source + "." + sourceSplitArray[i + 1].replace("0", "" + j) + providerTemplate.getSource().substring(index);
                                            if (destination != null) {
                                                StringBuilder destination1 = new StringBuilder(destination);
                                                destination1.append(providerTemplate.getDestination().substring(oneIndex + 1));
                                                String result = destination1.toString().replace("#", "" + j);
                                                templates.put(result, source1);
                                            } else {
                                                sourceSet.add(source1);
                                            }
                                            j++;
                                        }
                                    } else if (childNode != null) {
                                        int index = providerTemplate.getSource().indexOf(sourceSplitArray[i + 1]);
                                        source += providerTemplate.getSource().substring(index);
                                        if (destination != null) {
                                            StringBuilder destination1 = new StringBuilder(destination);
                                            destination1.append(providerTemplate.getDestination().substring(oneIndex + 1));
                                            destination1.toString().replace("#", "" + 0);
                                            templates.put(destination.toString(), source);
                                        } else
                                            sourceSet.add(source);
                                    }
                                    k++;
                                }
                            } else {
                                int k = 0;
                                while (k < childNode.size()) {
                                    String source = sourcePrefix + sourceSplitArray[i].replace("0", "" + k) + "." + sourceSplitArray[i + 1];
                                    sourceSet.add(source);
                                    k++;
                                }
                            }
                        }
                        break;
                    } else {
                        sourcePrefix.append(sourceSplitArray[i]).append(".");
                        childNode = childNode.get(sourceSplitArray[i]);
                    }
                    i++;
                }
                int k = 0;
                for (String str : sourceSet) {
                    String first = providerTemplate.getDestination().substring(0, providerTemplate.getDestination().lastIndexOf('0'));
                    String end = k + providerTemplate.getDestination().substring(providerTemplate.getDestination().lastIndexOf('0') + 1);
                    String destination = first + end;
                    templates.put(destination, str);
                    k++;
                }
            }
        }
        log.info("[getproviderTemplates] End Templates :" + templates);
        return templates;
    }

    public Map<String, String> getConfigMap(Long partnerId, String groupName) {
        List<ProviderUrlConfigEntity> partnerConfigList = providerUrlConfigEntityService.getUrlConfig(partnerId, groupName);
        Map<String, String> configMap = null;
        if (partnerConfigList != null)
            configMap = partnerConfigList.stream()
                    .collect(Collectors.toMap(ProviderUrlConfigEntity::getKey, ProviderUrlConfigEntity::getValue));
        return configMap;
    }
}
