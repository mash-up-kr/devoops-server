package com.devoops;

import com.devoops.config.TestConfig;
import com.devoops.fake.FakeTokenRepository;
import com.devoops.fake.FakeUserRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import(TestConfig.class)
@ExtendWith(DataBaseCleaner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FakeUserRepository userRepository;

    @Autowired
    private FakeTokenRepository tokenRepository;

    @BeforeEach
    void setEnvironment() {
        RestAssured.port = port;
    }

    @BeforeEach
    void clearDatabase() {
        userRepository.clear();
        tokenRepository.clear();
    }
}
