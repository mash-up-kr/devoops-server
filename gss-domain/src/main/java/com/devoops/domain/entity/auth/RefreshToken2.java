package com.devoops.domain.entity.auth;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RefreshToken2 {

    private long userId;
    private String value;
    private Duration ttl;

    public RefreshToken2(long userId, Duration ttl) {
        this(userId, UUID.randomUUID().toString(), ttl);
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
