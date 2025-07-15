package com.devoops.config;


import com.devoops.client.PrAnalysisClient;
import com.devoops.fake.FakePrAnalysisClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile(("test"))
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PrAnalysisClient prAnalysisClient() {
        return new FakePrAnalysisClient();
    }
}
