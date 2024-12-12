package com.sr.capital.filter;

import com.sr.capital.CommonConstant;
import com.sr.capital.config.AppProperties;
import com.sr.capital.util.AESUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.sr.capital.util.AESUtil.decrypt;

@Component
public class PathParamDecryptionFilter implements Filter {

    @Autowired
    AppProperties appProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        if (request instanceof HttpServletRequest httpRequest && appProperties.getIsEncryptionEnabled().equals(CommonConstant.TRUE)) {

            if(!CommonConstant.EXCLUDE_FROM_DECRYPTION.matches(httpRequest)) {
                // Get the URI and decrypt path parameters if present
                String decryptedPath = decryptPath(httpRequest.getRequestURI(), request, response);

                // Wrap the request with the modified URI
                HttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpRequest, decryptedPath);

                // Proceed with the filter chain using the modified request
                chain.doFilter(requestWrapper, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    /**
     * Decrypt path parameters in the URI.
     *
     * @param uri      The original URI.
     * @param request
     * @param response
     * @return The URI with decrypted path parameters.
     */
    private String decryptPath(String uri, ServletRequest request, ServletResponse response) {
        // Split the URI into parts, decrypting each segment if necessary
        String[] segments = uri.split("/");
        for (int i = 0; i < segments.length; i++) {
            try {
                String iv = AESUtil.getIvValue(request, response);

                // Decrypt each segment and replace in the URI
                segments[i] = decrypt(segments[i], appProperties.getAesSecretKey(), iv, appProperties.getAesIVKey());
            } catch (Exception e) {
                // Skip segments that are not encrypted
            }
        }
        return String.join("/", segments);
    }

    /**
     * Custom request wrapper to modify the URI.
     */
    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String decryptedPath;

        public CustomHttpServletRequestWrapper(HttpServletRequest request, String decryptedPath) {
            super(request);
            this.decryptedPath = decryptedPath;
        }

        @Override
        public String getRequestURI() {
            return decryptedPath;
        }
    }
}
