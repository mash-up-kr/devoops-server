package com.devoops.generator;

import com.devoops.domain.entity.github.token.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGenerator {

    @Autowired
    private UserDomainRepository userRepository;

    public User generate(String name) {
        GithubToken token = new GithubToken(name);
        User user = new User(ThreadLocalRandom.current().nextLong(), name, "profileUrl", token);
        return userRepository.saveUser(user);
    }
}
