package com.sr.capital;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class CommonConstant {
    public static final String ALGORITHM = "AES";
    public static final int KEY_SIZE = 256; // AES-256
    public static final String AES_CBC_PADDING = "AES/CBC/PKCS5Padding";
    public static final RequestMatcher EXCLUDE_FROM_DECRYPTION = new OrRequestMatcher(
            new AntPathRequestMatcher("/*/health/**"),
            new AntPathRequestMatcher("/*/cache/**"),
            new AntPathRequestMatcher("/**/external/**"),
            new AntPathRequestMatcher("/api/v1/kyc/doc/upload_and_extract/**"),
            new AntPathRequestMatcher("/aes/decrypt/**"),
            new AntPathRequestMatcher("/aes/encrypt/**"),
            new AntPathRequestMatcher("/api/file-upload/**"),
            new AntPathRequestMatcher("/**/icrm/**")

    );
    public static final String TRUE = "true";

    public static final String AUTOMATIC = "AUTOMATIC";
    public static final String MANUAL = "MANUAL";
    public static final String DELETED = "DELETED";
    public static final String INTERNAL_LOAN_ID = "%%INTERNAL_LOAN_ID%%";
    public static final String PARTNER = "%%PARTNER%%";
    public static final String KEY = "%%KEY%%";
    public static final String TIME = "%%TIME%%";
    public static final String REFRESHED = "REFRESHED";
}
