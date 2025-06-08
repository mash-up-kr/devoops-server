package com.devoops.jpa.repository.github;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.jpa.entity.github.GithubTokenEntity;
import com.devoops.jpa.entity.user.UserEntity;
import com.devoops.jpa.repository.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GithubTokenDomainRepositoryImpl implements GithubTokenDomainRepository {

    private final GithubTokenJpaRepository githubTokenJpaRepository;
    private final UserJpaRepository userJpaRepository;

    @Override
    public GithubToken save(GithubToken token, User owner) {
        UserEntity userEntity = userJpaRepository.findByProviderId(owner.getProviderId())
                .orElseThrow(() -> new RuntimeException("회원이 존재하지 않습니다")); //TODO 커스텀 에러 전환)

        return githubTokenJpaRepository.save(GithubTokenEntity.from(userEntity, token))
                .toDomainEntity();
    }
}
