package com.devoops.domain.repository.user;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findById(Long id);

    boolean existsById(Long id);

    boolean existsByProviderId(Long externalId);

    User saveUser(User user);

    User findByProviderId(Long providerId);
}
