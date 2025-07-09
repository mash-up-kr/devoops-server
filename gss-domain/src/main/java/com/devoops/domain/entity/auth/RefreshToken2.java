package com.devoops.domain.entity.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken2 {

    private final long userId;
    private final String value;
    private final Duration ttl;

    public RefreshToken2(long userId, Duration ttl) {
        this.userId = userId;
        this.value = UUID.randomUUID().toString();
        this.ttl = ttl;
    }

    public RefreshToken2 refresh() {
        return new RefreshToken2(this.userId, UUID.randomUUID().toString(), this.ttl);
    }

    public boolean isExpired() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiredAt = now.plusSeconds(ttl.getSeconds());
        return expiredAt.isBefore(now);
    }
}
