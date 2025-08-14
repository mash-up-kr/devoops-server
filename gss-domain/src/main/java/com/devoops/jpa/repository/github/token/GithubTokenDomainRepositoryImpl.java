package com.devoops.jpa.repository.github.token;

import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.token.GithubTokenDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.token.GithubTokenEntity;
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
    @Transactional(readOnly = true)
    public GithubToken getByUserId(long userId) {
        return githubTokenJpaRepository.findByUserId(userId)
                .map(GithubTokenEntity::toDomainEntity)
                .orElseThrow(() -> new GssException(ErrorCode.GITHUB_TOKEN_NOT_FOUND));
    }
}
