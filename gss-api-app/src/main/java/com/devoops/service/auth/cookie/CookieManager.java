package com.devoops.service.auth.cookie;

import java.time.Duration;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

    private static final String PATH = "/";
    private static final String SAME_SITE = SameSite.NONE.attributeValue();
    private static final String EMPTY_TOKEN = "";
    private static final Duration EXPIRED_DURATION = Duration.ZERO;

    public ResponseCookie createCookie(String key, String value, Duration expiration) {
        return ResponseCookie.from(key, value)
                .maxAge(expiration)
                .path(PATH)
                .sameSite(SAME_SITE)
                .secure(true)
                .build();
    }

    public ResponseCookie createExpiredCookie(String key) {
        return createCookie(key, EMPTY_TOKEN, EXPIRED_DURATION);
    }
}
