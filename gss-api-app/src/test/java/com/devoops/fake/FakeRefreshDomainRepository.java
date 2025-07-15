package com.devoops.fake;

import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FakeRefreshDomainRepository implements RefreshTokenDomainRepository {

    private final List<RefreshToken> refreshTokens = new ArrayList<>();

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        refreshTokens.add(refreshToken);
        return refreshToken;
    }

    @Override
    public Optional<RefreshToken> getRefreshToken(String tokenValue) {
        return refreshTokens.stream()
                .filter(token -> token.getValue().equals(tokenValue))
                .findAny();
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
