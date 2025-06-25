package com.devoops.domain.repository.user;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findByProviderId(long providerId);

    boolean existsByProviderId(long providerId);

    User findById(long providerId);

    boolean existsById(long providerId);

    User saveUser(User user);
}
