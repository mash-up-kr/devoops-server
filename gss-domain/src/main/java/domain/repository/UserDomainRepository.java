package domain.repository;

import domain.entity.User;

public interface UserDomainRepository {

    User find(String externalId);
}
