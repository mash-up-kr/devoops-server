package com.devoops.service.user;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class UserService {

    private final UserDomainRepository userDomainRepository;

    public User save(User user) {
        String userExternalId = user.getExternalId();
        if (userDomainRepository.existsByExternalId(userExternalId)) {
            return userDomainRepository.findByExternalId(userExternalId);
        }

        return userDomainRepository.saveUser(user);
    }
}
