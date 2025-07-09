package com.devoops.domain.entity.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshToken2 {

    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30L);

    private final long userId;
    private final String value;
    private final LocalDateTime expiredAt;

    public Duration getTtl() {
        return REFRESH_TOKEN_TTL;
    }
}
