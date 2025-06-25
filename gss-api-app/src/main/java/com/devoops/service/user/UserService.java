package com.devoops.service.user;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.support.PropertiesLoaderSupport;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainRepository userDomainRepository;
    private final GithubTokenDomainRepository tokenDomainRepository;

    public User save(User user, GithubToken token) {
        long providerId = user.getProviderId();
        if (userDomainRepository.existsByProviderId(providerId)) {
            return userDomainRepository.findByProviderId(providerId);
        }
        User savedUser = userDomainRepository.saveUser(user);
        tokenDomainRepository.save(token, savedUser); //TODO 논의사항 : 연관관계에 따른 저장 순서 의존
        return savedUser;
    }

    public User findByProviderId(long providerId) {
        return userDomainRepository.findByProviderId(providerId);
    }
}
