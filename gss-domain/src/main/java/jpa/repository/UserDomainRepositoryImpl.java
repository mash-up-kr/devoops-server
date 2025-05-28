package jpa.repository;

import domain.entity.User;
import domain.repository.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class UserDomainRepositoryImpl implements UserDomainRepository {
    private UserJpaRepository userJpaRepository;

    @Transactional(readOnly = true)
    @Override
    public User find(String externalId) {
        return userJpaRepository.findByExternalId(externalId).toDomainEntity();
    }
}
