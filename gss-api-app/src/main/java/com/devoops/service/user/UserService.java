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
        Long userId = user.getId();
        if (userId != null && userDomainRepository.existsById(userId)) {
            return userDomainRepository.findById(userId);
        }
        return userDomainRepository.saveUser(user);
    }

    public User findById(long id) {
        return userDomainRepository.findById(id);
    }
}
