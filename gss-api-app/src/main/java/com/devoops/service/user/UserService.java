package com.devoops.service.user;

import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.github.GithubTokenDomainRepository;
import com.devoops.domain.repository.user.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainRepository userDomainRepository;
    private final GithubTokenDomainRepository tokenDomainRepository;

    public User save(User user, GithubToken token) {
        long userId = user.getId();
        if (userDomainRepository.existsById(userId)) {
            return userDomainRepository.findById(userId);
        }
        User savedUser = userDomainRepository.saveUser(user);
        tokenDomainRepository.save(token, savedUser); //TODO 논의사항 : 연관관계에 따른 저장 순서 의존
        return savedUser;
    }

    public User findById(long id) {
        return userDomainRepository.findById(id);
    }
}
