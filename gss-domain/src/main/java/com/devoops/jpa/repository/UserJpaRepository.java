package com.devoops.jpa.repository;

import com.devoops.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByExternalId(String externalId);
}
