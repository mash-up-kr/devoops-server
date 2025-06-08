package com.devoops.domain.repository;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findByProviderId(long providerId);

    User saveUser(User user);
}
