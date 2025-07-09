package com.devoops.fake;

import com.devoops.domain.entity.auth.RefreshToken2;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeRefreshDomainRepository implements RefreshTokenDomainRepository {

    private final List<RefreshToken2> refreshTokens = new ArrayList<>();

    @Override
    public RefreshToken2 save(RefreshToken2 refreshToken) {
        refreshTokens.add(refreshToken);
        return refreshToken;
    }

    @Override
    public boolean exists(String refreshToken) {
        return refreshTokens.stream()
                .anyMatch(token -> token.getValue().equals(refreshToken));
    }

    @Override
    public RefreshToken2 getRefreshToken(String tokenValue) {
        return refreshTokens.stream()
                .filter(token -> token.getValue().equals(tokenValue))
                .findAny()
                .orElseThrow(() -> new RuntimeException("리프레시 토큰이 존재하지 않습니다"));
    }

    @Override
    public void delete(long userId) {
        refreshTokens.stream()
                .filter(token -> token.getUserId() == userId)
                .findAny()
                .ifPresent(refreshTokens::remove);
    }

    public void clear() {
        refreshTokens.clear();
    }
}
