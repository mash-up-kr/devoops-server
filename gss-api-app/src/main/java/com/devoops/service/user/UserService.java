package com.devoops.service.user;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDomainRepository userDomainRepository;

    public User save(User user) {
        String email = user.getEmail();
        if (userDomainRepository.existsByEmail(email)) {
            return userDomainRepository.findByEmail(email);
        }

        return userDomainRepository.saveUser(user);
    }
}
