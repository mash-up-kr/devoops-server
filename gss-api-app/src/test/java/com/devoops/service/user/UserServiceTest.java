package com.devoops.service.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.devoops.BaseServiceTest;
import com.devoops.domain.entity.github.GithubToken;
import com.devoops.domain.entity.user.User;
import com.devoops.domain.repository.user.UserDomainRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserServiceTest extends BaseServiceTest {

    @Autowired
    private UserDomainRepository userRepository;

    @Autowired
    private UserService userService;

    @Nested
    class SaveUser {

        @Test
        void 신규_유저를_저장한다() {
            User user = new User(1L, "nickname", "profile_url");
            GithubToken token = new GithubToken("token");

            User saveUser = userService.save(user, token);

            boolean isExist = userRepository.existsById(saveUser.getId());

            assertThat(isExist).isTrue();
        }

        @Test
        void 이미_존재하는_유저라면_기존유저를_반환한다() {
            User user = new User(1L, "nickname", "profile_url");
            GithubToken token = new GithubToken("token");
            User existsUser = userRepository.saveUser(user);

            User saveUser = userService.save(existsUser, token);

            assertThat(saveUser).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(existsUser);
        }
    }
}
