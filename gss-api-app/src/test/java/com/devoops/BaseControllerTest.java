package com.devoops;


import com.devoops.fake.FakeBlackListRepository;
import com.devoops.fake.FakeRefreshDomainRepository;
import com.devoops.generator.AnswerGenerator;
import com.devoops.generator.GithubRepoGenerator;
import com.devoops.generator.PullRequestGenerator;
import com.devoops.generator.QuestionGenerator;
import com.devoops.generator.UserGenerator;
import com.devoops.service.auth.TokenManager;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(DataBaseCleaner.class)
@Import(RedisTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    protected TokenManager tokenManager;

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

    @Autowired
    private FakeRefreshDomainRepository refreshDomainRepository;

    @Autowired
    private FakeBlackListRepository blackListRepository;

    @BeforeEach
    void setEnvironment() {
        RestAssured.port = port;
    }

    @BeforeEach
    void setup() {
        refreshDomainRepository.clear();
        blackListRepository.clear();
    }
}
