package com.devoops.domain.repository;


import com.devoops.domain.entity.User;

public interface UserDomainRepository {

    User find(String externalId);
}
