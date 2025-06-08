package com.devoops.jpa.repository;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.UserDomainRepository;
import com.devoops.jpa.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDomainRepositoryImpl implements UserDomainRepository {
    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    @Override
    public User findByExternalId(String externalId) {
        return userJpaRepository.findByExternalId(externalId)
                .orElseThrow(() -> new RuntimeException("EntityNotFound 공통 예외 처리 필요"))
                .toDomainEntity();
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userJpaRepository.save(
                UserEntity.from(user)
        ).toDomainEntity();
    }
}
