package com.devoops.fake;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FakeTokenRepository implements GithubTokenDomainRepository {

    private final List<GithubToken> tokens = new ArrayList<>();

    @Override
    public GithubToken save(GithubToken token, User user) {
        tokens.add(token);
        return token;
    }

    public void clear() {
        tokens.clear();
    }
}
