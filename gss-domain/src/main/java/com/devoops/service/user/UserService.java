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
        long providerId = user.getProviderId();
        if (userDomainRepository.existsByProviderId(providerId)) {
            return userDomainRepository.findByProviderId(providerId);
        }
        return userDomainRepository.saveUser(user);
    }

    public User findById(long id) {
        return userDomainRepository.findById(id);
    }

    public User findByProviderId(long providerId) {
        return userDomainRepository.findByProviderId(providerId);
    }

}
