package com.devoops.generator;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGenerator {

    @Autowired
    private UserDomainRepository userRepository;

    public User generate(String name) {
        User user = new User(1L, name, "profileUrl");
        return userRepository.saveUser(user);
    }
}
