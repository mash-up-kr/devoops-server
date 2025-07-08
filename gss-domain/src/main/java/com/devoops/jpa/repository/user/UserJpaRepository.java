package com.devoops.jpa.repository.user;

import com.devoops.jpa.entity.user.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByProviderId(Long id);

    Optional<UserEntity> findByProviderId(Long providerId);
}
