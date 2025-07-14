package com.devoops.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.devoops.config.BaseRedisTest;
import com.devoops.config.RedisConfig;
import com.devoops.domain.entity.auth.RefreshToken;
import com.devoops.domain.repository.auth.RefreshTokenDomainRepository;
import com.devoops.redis.auth.RefreshTokenDomainRepositoryImpl;
import java.time.Duration;
import java.util.UUID;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

class RefreshTokenDomainRepositoryTest extends BaseRedisTest {

    @Autowired
    private RefreshTokenDomainRepository refreshTokenDomainRepository;

    @Nested
    class CRUD {
        @Test
        void 리프레시_토큰을_저장할_수_있다() {
            RefreshToken refreshToken = new RefreshToken(1L, Duration.ofSeconds(1L));
            assertThatCode(() -> refreshTokenDomainRepository.save(refreshToken))
                    .doesNotThrowAnyException();
        }

        @Test
        void 리프레시_토큰의_저장_유무를_알_수_있다() {
            RefreshToken refreshToken = new RefreshToken(1L, Duration.ofSeconds(1L));
            refreshTokenDomainRepository.save(refreshToken);

            boolean exists = refreshTokenDomainRepository.getRefreshToken(refreshToken.getValue()).isPresent();
            boolean nonExists = refreshTokenDomainRepository.getRefreshToken(UUID.randomUUID().toString()).isPresent();

            assertAll(
                    () -> assertThat(exists).isTrue(),
                    () -> assertThat(nonExists).isFalse()
            );
        }

        @Test
        void 리프레시_토큰_값을_가져올_수_있다() {
            RefreshToken refreshToken = new RefreshToken(1L, Duration.ofSeconds(1L));
            refreshTokenDomainRepository.save(refreshToken);

            RefreshToken foundToken = refreshTokenDomainRepository.getRefreshToken(refreshToken.getValue()).get();

            assertThat(foundToken).usingRecursiveComparison()
                    .isEqualTo(refreshToken);
        }

        @Test
        void 리프레시_토큰을_삭제할_수_있다() {
            RefreshToken refreshToken = new RefreshToken(1L, Duration.ofSeconds(1L));
            refreshTokenDomainRepository.save(refreshToken);

            refreshTokenDomainRepository.delete(refreshToken.getValue());

            boolean exists = refreshTokenDomainRepository.getRefreshToken(refreshToken.getValue()).isPresent();

            assertThat(exists).isFalse();
        }
    }
}
