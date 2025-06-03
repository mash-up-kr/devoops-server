package com.devoops.domain.repository.user;


import com.devoops.domain.entity.user.User;

public interface UserDomainRepository {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    User saveUser(User user);
}
