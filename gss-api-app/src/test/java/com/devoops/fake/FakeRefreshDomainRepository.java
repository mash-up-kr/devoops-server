package com.devoops.fake;

import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeRefreshDomainRepository implements RefreshTokenDomainRepository {

    private final List<RefreshToken> refreshTokens = new ArrayList<>();

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        System.out.println("===========안 들어옴");
        refreshTokens.add(refreshToken);
        return refreshToken;
    }

    @Override
    public boolean exists(String refreshToken) {
        return refreshTokens.stream()
                .anyMatch(token -> token.getValue().equals(refreshToken));
    }

    @Override
    public RefreshToken getRefreshToken(String tokenValue) {
        return refreshTokens.stream()
                .filter(token -> token.getValue().equals(tokenValue))
                .findAny()
                .orElseThrow(() -> new RuntimeException("리프레시 토큰이 존재하지 않습니다"));
    }

    @Override
    public void delete(String tokenValue) {
        refreshTokens.stream()
                .filter(token -> token.getValue().equals(tokenValue))
                .findAny()
                .ifPresent(refreshTokens::remove);
    }

    public void clear() {
        refreshTokens.clear();
    }
}
