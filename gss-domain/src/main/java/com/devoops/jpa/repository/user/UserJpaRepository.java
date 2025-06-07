package com.devoops.jpa.repository.user;

import com.devoops.jpa.entity.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByProviderId(long providerId);

    boolean existsByProviderId(long providerId);
}
