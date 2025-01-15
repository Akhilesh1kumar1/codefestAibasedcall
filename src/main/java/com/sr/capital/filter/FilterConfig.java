package com.sr.capital.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration(CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(10); // Ensure CORS filter runs first
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<GoogleCaptchaValidator> googleCaptchaValidatorRegistration(GoogleCaptchaValidator corsFilter) {
        FilterRegistrationBean<GoogleCaptchaValidator> registrationBean = new FilterRegistrationBean<>(corsFilter);
        registrationBean.setOrder(20); // Ensure CORS filter runs first
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<PayloadDecryptionFilter> payloadDecryptionFilterRegistration(PayloadDecryptionFilter payloadDecryptionFilter) {
        FilterRegistrationBean<PayloadDecryptionFilter> registrationBean = new FilterRegistrationBean<>(payloadDecryptionFilter);
        registrationBean.setOrder(30); // Decryption filter runs after CORS
        return registrationBean;
    }
    @Bean
    public FilterRegistrationBean<PathParamDecryptionFilter> pathParamDecryptionFilterFilterRegistrationBean(PathParamDecryptionFilter payloadDecryptionFilter) {
        FilterRegistrationBean<PathParamDecryptionFilter> registrationBean = new FilterRegistrationBean<>(payloadDecryptionFilter);
        registrationBean.setOrder(40); // Decryption filter runs after CORS
        return registrationBean;
    }
}
