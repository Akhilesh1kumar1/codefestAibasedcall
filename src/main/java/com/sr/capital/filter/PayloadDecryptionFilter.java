package com.sr.capital.filter;

import com.sr.capital.CommonConstant;
import com.sr.capital.config.AppProperties;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static com.sr.capital.util.AESUtil.*;

@Configuration
@Order(2)
public class PayloadDecryptionFilter implements Filter {

    @Autowired
    AppProperties appProperties;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {


        if (request instanceof HttpServletRequest httpRequest && appProperties.getIsEncryptionEnabled().equals(CommonConstant.TRUE) ) {

            if(!CommonConstant.EXCLUDE_FROM_DECRYPTION.matches(httpRequest)) {
                String iv = getIvValue(request, response);
                // Decrypt the request payload
                String decryptedRequestBody = decryptRequest(httpRequest, iv);

                // Wrap the request with the decrypted payload
                HttpServletRequestWrapper requestWrapper = new CustomHttpServletRequestWrapper(httpRequest, decryptedRequestBody);

                // Proceed with the filter chain using the modified request
                chain.doFilter(requestWrapper, response);
                return;
            }
        }
        chain.doFilter(request, response);
    }



    private String decryptRequest(HttpServletRequest request, String iv) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return decrypt(removeExtraQuotes(body.replace("@", "/")), appProperties.getAesSecretKey(), iv, appProperties.getAesIVKey());
    }

    private static class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {
        private final String body;

        public CustomHttpServletRequestWrapper(HttpServletRequest request, String body) {
            super(request);
            this.body = body;
        }

        @Override
        public ServletInputStream getInputStream() {
            return new ServletInputStreamWrapper(body.getBytes());
        }

        @Override
        public BufferedReader getReader() {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }
    }

    private static class ServletInputStreamWrapper extends ServletInputStream {
        private final ByteArrayInputStream stream;

        public ServletInputStreamWrapper(byte[] data) {
            this.stream = new ByteArrayInputStream(data);
        }

        @Override
        public boolean isFinished() {
            return stream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
        }

        @Override
        public int read() {
            return stream.read();
        }
    }
}
