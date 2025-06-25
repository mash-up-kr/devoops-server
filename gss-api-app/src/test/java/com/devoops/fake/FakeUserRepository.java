package com.devoops.fake;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class FakeUserRepository implements UserDomainRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User findById(long id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User with providerId " + id + " not found"));
    }

    @Override
    public boolean existsById(long id) {
        return users.stream()
                .anyMatch(user -> user.getId() == id);
    }

    @Override
    public User findByProviderId(long providerId) {
        return users.stream()
                .filter(user -> user.getProviderId() == providerId)
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User with providerId " + providerId + " not found"));
    }

    @Override
    public boolean existsByProviderId(long providerId) {
        return users.stream()
                .anyMatch(user -> user.getProviderId() == providerId);
    }

    @Override
    public User saveUser(User user) {
        users.add(user);
        return user;
    }
}
