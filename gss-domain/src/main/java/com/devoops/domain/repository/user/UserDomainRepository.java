package com.devoops.domain.repository.user;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findByEmail(String externalId);

    boolean existsByEmail(String externalId);

    User saveUser(User user);
}
