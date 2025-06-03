package com.devoops.config;

import com.devoops.domain.repository.user.UserDomainRepository;
import com.devoops.fake.FakeUserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@Profile("test")
@TestConfiguration
public class TestConfig {

    @Bean
    public UserDomainRepository userDomainRepository() {
        return new FakeUserRepository();
    }
}
