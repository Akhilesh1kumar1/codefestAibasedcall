package com.sr.capital.config.properties;

import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
public class BypassApiDetails {

    private List<HttpMethod> method;
    private Boolean captchaEnabled = Boolean.FALSE;
    private String captchaAction;

}
