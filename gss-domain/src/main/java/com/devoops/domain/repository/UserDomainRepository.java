package com.devoops.domain.repository;


import com.devoops.domain.entity.User;

public interface UserDomainRepository {

    User findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);

    User saveUser(User user);
}
