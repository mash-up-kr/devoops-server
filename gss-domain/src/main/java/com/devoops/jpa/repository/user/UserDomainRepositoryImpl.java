package com.devoops.jpa.repository.user;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.jpa.entity.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class UserDomainRepositoryImpl implements UserDomainRepository {
    private final UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    @Override
    public User findById(long id) {
        return userJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("EntityNotFound 공통 예외 처리 필요"))
                .toDomainEntity();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsById(long id) {
        return userJpaRepository.existsByProviderId(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByProviderId(long providerId) {
        return userJpaRepository.findByProviderId(providerId)
                .orElseThrow(() -> new RuntimeException("EntityNotFound 공통 예외 처리 필요"))
                .toDomainEntity();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean existsByProviderId(long providerId) {
        return userJpaRepository.existsByProviderId(providerId);
    }

    @Transactional
    @Override
    public User saveUser(User user) {
        return userJpaRepository.save(
                UserEntity.from(user)
        ).toDomainEntity();
    }
}
