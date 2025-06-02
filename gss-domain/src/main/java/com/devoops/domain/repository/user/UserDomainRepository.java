package com.devoops.domain.repository.user;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findByExternalId(String externalId);

    boolean existsByExternalId(String externalId);

    User saveUser(User user);
}
