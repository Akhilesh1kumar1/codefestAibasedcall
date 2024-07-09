package com.sr.capital.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CORSConfiguration {

    @Value("${app.cors-allowed-origins}")
    private List<String> allowedOrigins;

    @Bean
    public CorsWebFilter corsWebFilter() {

        final CustomCorsConfiguration corsConfig = new CustomCorsConfiguration();
        corsConfig.setAllowedOrigins(allowedOrigins);
        corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        corsConfig.addAllowedHeader("*");
       // corsConfig.setExposedHeaders(HeaderConstants.exposedResponseHeaders);

        /*final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);*/

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source) ;
    }

    public static class CustomCorsConfiguration extends CorsConfiguration {

        @Override
        public String checkOrigin(String requestOrigin) {
            if (requestOrigin != null && requestOrigin.startsWith("chrome-extension:")) {
                return requestOrigin;
            }
            return super.checkOrigin(requestOrigin);
        }
    }
}