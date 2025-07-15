package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.exception.custom.GssException;
import com.devoops.exception.errorcode.ErrorCode;
import com.devoops.jpa.entity.github.GithubTokenEntity;
import com.devoops.jpa.entity.user.UserEntity;
import com.devoops.jpa.repository.user.UserJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class GithubTokenDomainRepositoryImpl implements GithubTokenDomainRepository {

    private final GithubTokenJpaRepository githubTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    @Transactional
    public GithubToken save(GithubToken token, User owner) {
        UserEntity userEntity = userJpaRepository.findById(owner.getId())
                .orElseThrow(() -> new GssException(ErrorCode.USER_NOT_FOUND));

        return githubTokenJpaRepository.save(GithubTokenEntity.from(userEntity, token))
                .toDomainEntity();
    }

    @Override
    public Optional<GithubToken> findByUserId(User owner) {

        return githubTokenJpaRepository.findByUser_Id(owner.getId())
                .map(GithubTokenEntity::toDomainEntity);
    }
}
