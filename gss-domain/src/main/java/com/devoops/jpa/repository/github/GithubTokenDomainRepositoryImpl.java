package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.jpa.entity.github.GithubTokenEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class GithubTokenDomainRepositoryImpl implements GithubTokenDomainRepository {

    private final GithubTokenJpaRepository githubTokenJpaRepository;

    @Override
    @Transactional
    public GithubToken save(GithubToken token, User owner) {
        return githubTokenJpaRepository.save(GithubTokenEntity.from(owner.getId(), token))
                .toDomainEntity();
    }

    @Override
    public Optional<GithubToken> findByUserId(User owner) {

        return githubTokenJpaRepository.findByUserId(owner.getId())
                .map(GithubTokenEntity::toDomainEntity);
    }
}
