package com.devoops.domain.repository.auth;

import java.util.Date;

public interface BlackListRepository {

    boolean isExists(String token);

    void addBlackList(String token, Date expiration);
}
