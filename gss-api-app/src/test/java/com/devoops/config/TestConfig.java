package com.devoops.config;

import com.devoops.client.GithubOAuthClient;
import com.devoops.fake.FakeOAuthClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile({"test", "ci"})
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public GithubOAuthClient githubOAuthClient() {
        return new FakeOAuthClient();
    }
}
