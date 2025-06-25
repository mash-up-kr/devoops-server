package com.devoops.fake;

import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

@Component
public class FakeUserRepository implements UserDomainRepository {

    private final AtomicLong idGenerator = new AtomicLong();
    private final List<User> users = new ArrayList<>();

    @Override
    public User findById(Long id) {
        System.out.println("findById");
        return users.stream()
                .filter(user -> Objects.equals(user.getId(), id))
                .findAny()
                .orElseThrow(() -> new NoSuchElementException("User with providerId " + id + " not found"));
    }

    @Override
    public boolean existsById(Long id) {
        System.out.println("existsById");
        return users.stream()
                .anyMatch(user -> Objects.equals(user.getId(), id));
    }

    @Override
    public User saveUser(User user) {
        System.out.println("saveUser");
        User saveUser = new User(
                idGenerator.incrementAndGet(),
                user.getProviderId(),
                user.getNickname(),
                user.getProfileImageUrl()
        );
        users.add(saveUser);
        return saveUser;
    }

    public void clear() {
        users.clear();
        idGenerator.set(0);
    }
}
