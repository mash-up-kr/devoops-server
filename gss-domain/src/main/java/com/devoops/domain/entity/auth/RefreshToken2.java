package com.devoops.domain.entity.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshToken2 {

    private static final Duration REFRESH_TOKEN_TTL = Duration.ofDays(30L);

    private final long userId;
    private final String value;
    private final LocalDateTime expiredAt;

    public RefreshToken2(long userId) {
        this.userId = userId;
        this.value = UUID.randomUUID().toString();
        this.expiredAt = LocalDateTime.now().plus(REFRESH_TOKEN_TTL);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        return expiredAt.isBefore(now);
    }

    public Duration getTtl() {
        return REFRESH_TOKEN_TTL;
    }
}
