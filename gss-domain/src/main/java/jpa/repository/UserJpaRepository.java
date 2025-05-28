package jpa.repository;

import jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {

    public UserEntity findByExternalId(String externalId);
}
