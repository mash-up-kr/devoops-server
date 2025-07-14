package com.devoops;

import com.devoops.generator.AnswerGenerator;
import com.devoops.generator.GithubRepoGenerator;
import com.devoops.generator.PullRequestGenerator;
import com.devoops.generator.QuestionGenerator;
import com.devoops.generator.UserGenerator;
import com.devoops.service.auth.jwt.JwtTokenManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(DataBaseCleaner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected JwtTokenManager jwtTokenManager;

    @Autowired
    protected UserGenerator userGenerator;

    @Autowired
    protected GithubRepoGenerator repoGenerator;

    @Autowired
    protected PullRequestGenerator pullRequestGenerator;

    @Autowired
    protected QuestionGenerator questionGenerator;

    @Autowired
    protected AnswerGenerator answerGenerator;

    @BeforeEach
    void setEnvironment() {
        RestAssured.port = port;
    }
}
