package com.devoops.jpa.repository.user;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.jpa.entity.github.GithubTokenEntity;
import com.devoops.jpa.entity.user.UserEntity;
import com.devoops.jpa.repository.github.GithubTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserDomainRepositoryImpl implements UserDomainRepository {

    private final UserJpaRepository userJpaRepository;
    private final GithubTokenJpaRepository githubTokenJpaRepository;

    @Transactional(readOnly = true)
    @Override
    public User findById(Long id) {
        GithubToken githubToken = githubTokenJpaRepository.findByUserId(id)
                .toDomainEntity();
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EntityNotFound 공통 예외 처리 필요"))
                .toDomainEntity(githubToken);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(Long id) {
        return userJpaRepository.existsById(id);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        UserEntity userEntity = UserEntity.from(user);
        UserEntity savedUser = userJpaRepository.save(userEntity);
        GithubTokenEntity githubToken = GithubTokenEntity.from(savedUser, user.getGithubToken());
        GithubTokenEntity savedGithubToken = githubTokenJpaRepository.save(githubToken);
        return savedUser.toDomainEntity(savedGithubToken.toDomainEntity());
    }
}
