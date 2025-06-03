package com.devoops.fake;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class FakeUserRepository implements UserDomainRepository {

    private final List<User> users = new ArrayList<>();

    @Override
    public User findByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User with email " + email + " not found"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return users.stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

    @Override
    public User saveUser(User user) {
        users.add(user);
        return user;
    }
}
