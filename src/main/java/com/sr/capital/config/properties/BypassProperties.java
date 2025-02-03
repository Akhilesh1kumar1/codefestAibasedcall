package com.sr.capital.config.properties;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Pattern;

@ConfigurationProperties("bypass")
@ToString
@Data
@Component
public class BypassProperties {

    private Map<String, BypassApiDetails> apis;
    private Set<String> eligibleFilters = new HashSet<>();

    public Pair<Boolean, String> getGoogleCaptchaFlagAndAction(String path, HttpMethod method) {
        BypassApiDetails bypassApiDetails = this.apis.getOrDefault(path.replace("/", ""), null);
        if(bypassApiDetails == null) {
            return Pair.of(Boolean.FALSE, "");
        }

        if(ObjectUtils.isEmpty(bypassApiDetails)) {
            return Pair.of(Boolean.FALSE, "");
        }
        if(!CollectionUtils.isEmpty(bypassApiDetails.getMethod()) && bypassApiDetails.getMethod().contains(method)) {
            String action = "";
            if(StringUtils.hasLength(bypassApiDetails.getCaptchaAction())) {
                action = bypassApiDetails.getCaptchaAction();
            }
            return Pair.of(bypassApiDetails.getCaptchaEnabled(), action);
        }
        return Pair.of(Boolean.FALSE, "");
    }

}
