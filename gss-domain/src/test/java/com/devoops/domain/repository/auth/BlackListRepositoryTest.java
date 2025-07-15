package com.devoops.domain.repository.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.config.BaseRedisTest;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class BlackListRepositoryTest extends BaseRedisTest {

    @Autowired
    private BlackListRepository blackListRepository;

    @Nested
    class CRUD {

        @Test
        void 블랙_리스트에_토큰을_저장할_수_있다() {
            Date expiration = Date.from(Instant.now().plusSeconds(1L));
            String token = UUID.randomUUID().toString();
            blackListRepository.addBlackList(token, expiration);

            boolean isExists = blackListRepository.isExists(token);
            boolean isNotExists = blackListRepository.isExists(UUID.randomUUID().toString());

            assertAll(
                    () -> assertThat(isExists).isTrue(),
                    () -> assertThat(isNotExists).isFalse()

            );
        }
    }
}
